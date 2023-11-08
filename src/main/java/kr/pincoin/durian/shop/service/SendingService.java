package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.Voucher;
import kr.pincoin.durian.shop.domain.conveter.*;
import kr.pincoin.durian.shop.repository.jpa.*;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemCountResult;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemProductResult;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemVoucherDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SendingService {
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderItemVoucherRepository orderItemVoucherRepository;

    private final ProductRepository productRepository;

    private final VoucherRepository voucherRepository;

    private final ProfileRepository profileRepository;

    public boolean
    sendVouchers(Long orderId, Long userId) {
        if (issueVouchers(orderId, userId)) {
            // send email and sms
            return true;
        }

        return false;
    }

    @Transactional
    public
    boolean issueVouchers(Long orderId, Long userId) {
        // 1. Find valid order
        Order order = orderRepository.findOrder(orderId,
                                                userId,
                                                OrderStatus.ORDERED,
                                                null,
                                                PaymentStatus.PAID,
                                                SendingStatus.NOT_SENT,
                                                OrderVisibility.VISIBLE,
                                                null,
                                                null,
                                                null,
                                                false)
                .orElseThrow();

        Profile profile = profileRepository.findMember(userId,
                                                       UserStatus.NORMAL)
                .orElseThrow();

        // 2. Stock available?

        // Fetch Entity or DTO
        // DTO projection enables to fetch specified fields, and does not need to transform into entity.
        // DTO result is `not managed` in persistence context.
        List<OrderItemProductResult> orderItemWithProducts = orderItemRepository
                .findOrderItemWithProduct(orderId, userId);

        boolean error = false;

        List<String> outOfStock = new ArrayList<>();

        for (OrderItemProductResult result : orderItemWithProducts) {
            if (result.getOrderItem().getQuantity() > result.getRemaining()) {
                error = true;

                outOfStock.add(String.format("%s: [%s/%s/%s]",
                                                result.getProduct().getSlug(),
                                                result.getOrderItem().getQuantity() - result.getRemaining(),
                                                result.getRemaining(),
                                                result.getOrderItem().getQuantity()));
            }
        }

        if (error) {
            log.warn("{}", outOfStock);
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Ouf of stock",
                                   outOfStock);
        }

        // 3. Already sent?
        List<OrderItemCountResult> orderItemCount = orderItemRepository
                .findVoucherCountByOrderItem(orderId, userId);

        List<String> alreadySent = new ArrayList<>();

        for (OrderItemCountResult result : orderItemCount) {
            if (result.getCount() > 0) {
                error = true;

                alreadySent.add(String.format("%s: [%s]",
                                                 result.getOrderItem().getSlug(),
                                                 result.getCount()));
            }
        }

        if (error) {
            log.warn("{}", alreadySent);
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Already sent",
                                   alreadySent);
        }

        // 4. Check user verification

        // 5. Mark as sent
        for (OrderItemProductResult result : orderItemWithProducts) {
            // 5-1. Find vouchers by product.id / orderItem.quantity
            List<Voucher> vouchers = voucherRepository.findVouchers(result.getProduct().getId(),
                                                                    null,
                                                                    List.of(VoucherStatus.PURCHASED),
                                                                    false,
                                                                    result.getOrderItem().getQuantity());

            // 5-2. Insert order item vouchers (JdbcTemplate bulk insert)
            orderItemVoucherRepository
                    .saveAll(vouchers.stream()
                                     .map(voucher -> new OrderItemVoucherDto(voucher.getCode(),
                                                                             voucher.getRemarks(),
                                                                             false,
                                                                             result.getOrderItem().getId(),
                                                                             voucher.getId()))
                                     .toList());

            // 5-3. Update set voucher.sold
            voucherRepository.changeStatus(vouchers.stream().map(Voucher::getId).toList(),
                                           VoucherStatus.SOLD);

            // 5-4. Update stock status
            productRepository.subtract(result.getProduct().getId(),
                                       result.getOrderItem().getQuantity());
        }

        // persistence context cleared

        // 6. Update order status
        order.changeSendingStatus(SendingStatus.SENT);

        // 7. Update profile
        profile.addTransaction(order.getTotalSellingPrice());

        return true;
    }
}
