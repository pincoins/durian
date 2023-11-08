package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.notification.controller.dto.LineNotifyRequest;
import kr.pincoin.durian.notification.service.AligoService;
import kr.pincoin.durian.notification.service.LineNotifyService;
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

    private final LineNotifyService lineNotifyService;

    private final AligoService aligoService;

    public void
    sendVouchers(Long orderId, Long userId) {
        // Fetch read-only entities
        Order order = getOrder(orderId, userId);

        Profile profile = getProfile(userId);

        List<OrderItemProductResult> orderItemWithProducts = getOrderItemProductResults(order, profile);

        // Transactional write
        issueVouchers(order, profile, orderItemWithProducts);

        // Send email and sms
    }

    @Transactional
    public void
    issueVouchers(Order order, Profile profile, List<OrderItemProductResult> orderItemWithProducts) {
        // 1. Mark as sent
        for (OrderItemProductResult result : orderItemWithProducts) {
            // 1-1. Find vouchers by product.id / orderItem.quantity
            List<Voucher> vouchers = voucherRepository.findVouchers(result.getProduct().getId(),
                                                                    null,
                                                                    List.of(VoucherStatus.PURCHASED),
                                                                    false,
                                                                    result.getOrderItem().getQuantity());

            if (vouchers.size() != result.getOrderItem().getQuantity()) {
                String message = String.format("%s is out of stock.", result.getProduct().getSlug());
                lineNotifyService.send(new LineNotifyRequest(message));
                throw new ApiException(HttpStatus.CONFLICT,
                                       "Ouf of stock",
                                       List.of(message));
            }

            // 2-2. Insert order item vouchers (JdbcTemplate bulk insert)
            orderItemVoucherRepository
                    .saveAll(vouchers.stream()
                                     .map(voucher -> new OrderItemVoucherDto(voucher.getCode(),
                                                                             voucher.getRemarks(),
                                                                             false,
                                                                             result.getOrderItem().getId(),
                                                                             voucher.getId()))
                                     .toList());

            // 3-3. Update set voucher.sold
            voucherRepository.changeStatus(vouchers.stream().map(Voucher::getId).toList(),
                                           VoucherStatus.SOLD);

            // 4-4. Update stock status
            productRepository.updateStockQuantity(result.getProduct()
                                                          .subtractStockQuantity(result.getOrderItem().getQuantity()));
        }

        // 5. Update order status
        orderRepository.updateSendingStatus(order.changeSendingStatus(SendingStatus.SENT));

        // 6. Update profile
        profileRepository.updateTransaction(profile.addTransaction(order.getTotalListPrice(),
                                                                   order.getTotalSellingPrice()));
    }


    public Order getOrder(Long orderId, Long userId) {
        return orderRepository.findOrder(orderId,
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
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                "Order not found",
                                                List.of("Requested order is not valid.")));
    }

    private Profile getProfile(Long userId) {
        return profileRepository.findMember(userId,
                                            UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Member not found",
                                                    List.of("Requested member is not valid.")));
    }


    private List<OrderItemProductResult> getOrderItemProductResults(Order order, Profile profile) {
        // 1. Stock available?
        // DTO result is `not managed` in persistence context.
        List<OrderItemProductResult> orderItemWithProducts = orderItemRepository
                .findOrderItemWithProduct(order.getId(), profile.getUser().getId());

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
            lineNotifyService.send(new LineNotifyRequest("Out of stock:\n" + String.join("\n", outOfStock)));
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Ouf of stock",
                                   outOfStock);
        }

        // 2. Already sent?
        List<OrderItemCountResult> orderItemCount = orderItemRepository
                .findVoucherCountByOrderItem(order.getId(), profile.getUser().getId());

        List<String> alreadySent = new ArrayList<>();

        for (OrderItemCountResult result : orderItemCount) {
            if (result.getCount() > 0) {
                error = true;

                alreadySent.add(String.format("%s: [%s]", result.getOrderItem().getSlug(), result.getCount()));
            }
        }

        if (error) {
            lineNotifyService.send(new LineNotifyRequest("Already sent:\n" + String.join("\n", alreadySent)));
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Already sent",
                                   alreadySent);
        }

        // 3. Check user verification

        return orderItemWithProducts;
    }
}
