package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.service.IdentityService;
import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import kr.pincoin.durian.shop.repository.jpa.OrderItemRepository;
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
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    private final IdentityService identityService;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<OrderItem> listOrderItems(Long orderId,
                                          Long userId,
                                          UserDetails userDetails) {


        return identityService.isAdmin(userDetails)
                ? orderItemRepository.findOrderItems(orderId,
                                                  userId,
                                                     null,
                                                     null,
                                                     null)
                : orderItemRepository.findOrderItems(orderId,
                                                     userId,
                                                     OrderStatus.ORDERED,
                                                     OrderVisibility.VISIBLE,
                                                     false);
    }
}
