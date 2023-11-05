package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.shop.domain.OrderPayment;
import kr.pincoin.durian.shop.repository.jpa.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentService {
    private final OrderPaymentRepository orderPaymentRepository;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<OrderPayment> listOrderPayments(Long orderId,
                                                Long userId) {
        return orderPaymentRepository.findOrderPayments(orderId,
                                                        userId);
    }
}
