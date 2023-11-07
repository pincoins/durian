package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import kr.pincoin.durian.shop.domain.conveter.PaymentStatus;
import kr.pincoin.durian.shop.domain.conveter.SendingStatus;
import kr.pincoin.durian.shop.repository.jpa.OrderItemRepository;
import kr.pincoin.durian.shop.repository.jpa.OrderRepository;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemCountResult;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemProductResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SendingService {
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

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

        // 1. Stock available?
        List<OrderItemProductResult> orderItemWithProducts = orderItemRepository
                .findOrderItemWithProduct(orderId, userId);

        boolean error = false;

        StringBuilder outOfStock = new StringBuilder();

        for (OrderItemProductResult result : orderItemWithProducts) {
            if (result.getOrderItem().getQuantity() > result.getRemaining()) {
                error = true;

                outOfStock.append(String.format("%s is out of stock [%s/%s/%s]\n",
                                                result.getProduct().getSlug(),
                                                result.getOrderItem().getQuantity() - result.getRemaining(),
                                                result.getRemaining(),
                                                result.getOrderItem().getQuantity()));
            }
        }

        if (error) {
            log.warn("{}", outOfStock);
        }

        // 2. Already sent?
        List<OrderItemCountResult> orderItemCount = orderItemRepository
                .findVoucherCountByOrderItem(orderId, userId);

        StringBuilder alreadySent = new StringBuilder();

        for (OrderItemCountResult result : orderItemCount) {
            if (result.getCount() > 0) {
                error = true;

                alreadySent.append(String.format("%s is already sent: [%s]\n",
                                                 result.getOrderItem().getSlug(),
                                                 result.getCount()));
            }
        }

        if (error) {
            log.warn("{}", alreadySent);
        }

        /////////////////////// validation

        // 3. Mark as sent

        // 4. Update stock status

        // 5. Update order status
        order.changeSendingStatus(SendingStatus.SENT);

        // 6. Update profile
        profile.addTransaction(order.getTotalSellingPrice());

        return true;
    }
}
