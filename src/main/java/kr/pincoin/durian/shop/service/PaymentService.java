package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.service.IdentityService;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.OrderPaymentCreateRequest;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.OrderPayment;
import kr.pincoin.durian.shop.domain.conveter.*;
import kr.pincoin.durian.shop.repository.jpa.OrderPaymentRepository;
import kr.pincoin.durian.shop.repository.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final OrderRepository orderRepository;

    private final OrderPaymentRepository orderPaymentRepository;

    private final IdentityService identityService;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<OrderPayment>
    listOrderPayments(Long orderId,
                      Long userId,
                      UserDetails userDetails) {
        return identityService.isAdmin(userDetails)
                ? orderPaymentRepository.findOrderPayments(orderId,
                                                           userId,
                                                           null,
                                                           null,
                                                           null)
                : orderPaymentRepository.findOrderPayments(orderId,
                                                           userId,
                                                           OrderStatus.ORDERED,
                                                           OrderVisibility.VISIBLE,
                                                           false);
    }

    @Transactional
    public Optional<OrderPayment>
    addPayment(OrderPaymentCreateRequest request) {
        Order order = orderRepository.findOrder(request.getOrderId(),
                                                request.getUserId(),
                                                OrderStatus.ORDERED,
                                                PaymentMethod.BANK_TRANSFER,
                                                PaymentStatus.UNPAID,
                                                SendingStatus.NOT_SENT,
                                                OrderVisibility.VISIBLE,
                                                null,
                                                null,
                                                null,
                                                false)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid order",
                                                    List.of("Normal order does not exist.")));

        OrderPayment orderPayment = OrderPayment.builder(request.getAccount(),
                                                         request.getAmount(),
                                                         BigDecimal.ZERO).build();
        orderPayment.belongsTo(order);
        orderPaymentRepository.save(orderPayment);

        return Optional.of(orderPayment);
    }

    @Transactional
    public boolean
    addPayment(String accountParam,
                              String receivedPram,
                              String nameParam,
                              String methodParam,
                              String amountParam,
                              String balanceParam) {
        if (StringUtils.hasText(accountParam)
                && StringUtils.hasText(receivedPram)
                && StringUtils.hasText(nameParam)
                && StringUtils.hasText(methodParam)
                && StringUtils.hasText(amountParam)
                && StringUtils.hasText(balanceParam)) {
            log.warn("Invalid payload: {} {} {} {} {} {}",
                     accountParam,
                     receivedPram,
                     nameParam,
                     methodParam,
                     amountParam,
                     balanceParam);
            return false;
        }

        BigDecimal amount = new BigDecimal(amountParam.replace(",", ""));
        BigDecimal balance = new BigDecimal(balanceParam.replace(",", ""));

        if (accountParam.equals(PaymentAccount.KB.getCode()) && methodParam.contains("전자결제입금")) {
            log.warn("KB escrow: {} {} {} {} {} {}",
                     accountParam,
                     receivedPram,
                     nameParam,
                     methodParam,
                     amountParam,
                     balanceParam);
            return false;
        }

        if (!methodParam.equals("입금")
                && !(accountParam.equals(PaymentAccount.KB.getCode())
                && methodParam.equals("리브머니") || methodParam.equals("제휴CD이체"))) {
            log.warn("Not paid: {} {} {} {} {} {}",
                     accountParam,
                     receivedPram,
                     nameParam,
                     methodParam,
                     amountParam,
                     balanceParam);
            return false;
        }

        List<Order> orders = orderRepository.findDistinctOrderWithItems(null,
                                                                        null,
                                                                        OrderStatus.ORDERED,
                                                                        PaymentMethod.BANK_TRANSFER,
                                                                        PaymentStatus.UNPAID,
                                                                        SendingStatus.NOT_SENT,
                                                                        OrderVisibility.VISIBLE,
                                                                        nameParam,
                                                                        amount,
                                                                        null,
                                                                        null,
                                                                        false);

        if (orders.isEmpty()) {
            log.warn("Order not found: {} {} {} {} {} {}",
                     accountParam,
                     receivedPram,
                     nameParam,
                     methodParam,
                     amountParam,
                     balanceParam);
            return false;
        }

        // All the same name, selling price and amount
        long duplicates = orders.stream()
                .filter(o -> o.getTotalSellingPrice().equals(amount))
                .map(o -> o.getUser().getId())
                .distinct()
                .count();

        if (orders.size() > 1 && duplicates != 1) {
            // given
            // user.id = 1, order.totalSellingPrice = 1000
            // user.id = 2, order.totalSellingPrice = 2000
            // user.id = 3, order.totalSellingPrice = 1000

            // when - 1000
            // then - 2 orders

            // when - 2000
            // then - 1 order found, send!

            // when - 3000
            // then - 0 orders

            log.warn("Duplicate name and amount: {} {} {} {} {} {}",
                     accountParam,
                     receivedPram,
                     nameParam,
                     methodParam,
                     amountParam,
                     balanceParam);
            return false;
        }

        orders.stream()
                .filter(o -> o.getTotalSellingPrice().compareTo(amount) < 0)
                .findFirst()
                .ifPresent(order -> {
                    OrderPayment payment = OrderPayment.builder(PaymentAccount.fromCode(accountParam),
                                                                amount,
                                                                balance).build();
                    payment.belongsTo(order);
                    orderPaymentRepository.save(payment);
                });

        return true;
    }
}
