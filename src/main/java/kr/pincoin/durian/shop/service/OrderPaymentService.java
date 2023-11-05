package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.service.IdentityService;
import kr.pincoin.durian.shop.domain.OrderPayment;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import kr.pincoin.durian.shop.repository.jpa.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentService {
    private final OrderPaymentRepository orderPaymentRepository;

    private final IdentityService identityService;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<OrderPayment> listOrderPayments(Long orderId,
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
}
