package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.service.IdentityService;
import kr.pincoin.durian.shop.domain.OrderItemVoucher;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import kr.pincoin.durian.shop.repository.jpa.OrderItemVoucherRepository;
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
public class OrderItemVoucherService {
    private final OrderItemVoucherRepository orderItemVoucherRepository;

    private final IdentityService identityService;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<OrderItemVoucher>
    listOrderItemVouchers(Long orderId,
                          Long userId,
                          Long itemId,
                          UserDetails userDetails) {
        return identityService.isAdmin(userDetails)
                ? orderItemVoucherRepository.findOrderItemVouchers(orderId,
                                                                   userId,
                                                                   itemId,
                                                                   null,
                                                                   null,
                                                                   null)
                : orderItemVoucherRepository.findOrderItemVouchers(orderId,
                                                                   userId,
                                                                   itemId,
                                                                   OrderStatus.ORDERED,
                                                                   OrderVisibility.VISIBLE,
                                                                   false);
    }
}
