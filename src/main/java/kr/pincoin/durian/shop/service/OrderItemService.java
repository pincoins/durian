package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.repository.jpa.OrderItemRepository;
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
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<OrderItem> listOrderItems(Long orderId,
                                          Long userId,
                                          Boolean removed) {
        return orderItemRepository.findOrderItems(orderId,
                                                  userId,
                                                  removed);
    }
}
