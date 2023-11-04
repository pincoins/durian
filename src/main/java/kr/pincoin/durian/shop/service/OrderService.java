package kr.pincoin.durian.shop.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.OrderCreateRequest;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.*;
import kr.pincoin.durian.shop.repository.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ProfileRepository profileRepository;

    private final OrderRepository orderRepository;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<Order> listOrders(Long userId,
                                  OrderStatus status,
                                  PaymentMethod paymentMethod,
                                  PaymentStatus payment,
                                  DeliveryStatus delivery,
                                  OrderVisibility visibility,
                                  String fullName,
                                  String orderUuid,
                                  String transactionId,
                                  Boolean removed) {
        return orderRepository.findOrders(userId,
                                          status,
                                          paymentMethod,
                                          payment,
                                          delivery,
                                          visibility,
                                          fullName,
                                          orderUuid,
                                          transactionId,
                                          removed);
    }

    @Transactional
    public Optional<Order>
    createOrder(OrderCreateRequest request, HttpServletRequest servletRequest) {
        Profile profile = profileRepository.findMember(request.getUserId(), UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid category",
                                                    List.of("Normal profile does not exist.")));

        Order order = Order.builder(PaymentMethod.BANK_TRANSFER, profile, servletRequest).build();
        orderRepository.save(order);

        return Optional.of(order);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public boolean deleteOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderRepository.delete(order);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Soft removed order not found",
                                                      List.of("Order does not exist to delete.")));
    }
}
