package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.OrderPayment;
import kr.pincoin.durian.shop.domain.conveter.*;
import kr.pincoin.durian.shop.repository.jpa.OrderPaymentRepository;
import kr.pincoin.durian.shop.repository.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final OrderRepository orderRepository;

    private final OrderPaymentRepository orderPaymentRepository;

    @Transactional
    public boolean addPayment(String accountParam,
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
            log.info("Invalid payload: {} {} {} {} {} {}",
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
            log.info("KB escrow: {} {} {} {} {} {}",
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
            log.info("Not paid: {} {} {} {} {} {}",
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
                                                                        DeliveryStatus.NOT_SENT,
                                                                        OrderVisibility.VISIBLE,
                                                                        nameParam,
                                                                        amount,
                                                                        null,
                                                                        null,
                                                                        false);

        if (orders.isEmpty()) {
            log.info("Order not found: {} {} {} {} {} {}",
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

        if (duplicates > 1) {
            log.info("Duplicate name and amount: {} {} {} {} {} {}",
                     accountParam,
                     receivedPram,
                     nameParam,
                     methodParam,
                     amountParam,
                     balanceParam);
        }

        Order order = orders.stream().filter(o -> o.getTotalSellingPrice().equals(amount)).findFirst().get();

        OrderPayment payment = OrderPayment.builder(PaymentAccount.fromString(accountParam), amount, balance).build();
        payment.belongsTo(order);
        orderPaymentRepository.save(payment);

        return true;
    }
}
