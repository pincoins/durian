package kr.pincoin.durian.shop.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.common.util.RequestHeaderParser;
import kr.pincoin.durian.shop.controller.dto.CartItemNested;
import kr.pincoin.durian.shop.controller.dto.OrderCreateRequest;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.conveter.*;
import kr.pincoin.durian.shop.repository.jpa.OrderRepository;
import kr.pincoin.durian.shop.repository.jpa.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final ProfileRepository profileRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final RequestHeaderParser requestHeaderParser;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<Order> listOrders(Long userId,
                                  OrderStatus status,
                                  PaymentMethod paymentMethod,
                                  PaymentStatus payment,
                                  SendingStatus sending,
                                  OrderVisibility visibility,
                                  String fullName,
                                  String orderUuid,
                                  String transactionId,
                                  Boolean removed) {
        return orderRepository.findOrders(userId,
                                         status,
                                         paymentMethod,
                                         payment,
                                         sending,
                                         visibility,
                                         fullName,
                                         orderUuid,
                                         transactionId,
                                         removed);
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public Optional<Order> getOrder(Long orderId,
                                    Long userId,
                                    OrderStatus status,
                                    PaymentMethod paymentMethod,
                                    PaymentStatus payment,
                                    SendingStatus sending,
                                    OrderVisibility visibility,
                                    String fullName,
                                    String orderUuid,
                                    String transactionId,
                                    Boolean removed) {
        Order order = orderRepository.findOrder(orderId,
                                                userId,
                                                status,
                                                paymentMethod,
                                                payment,
                                                sending,
                                                visibility,
                                                fullName,
                                                orderUuid,
                                                transactionId,
                                                removed)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Order not found",
                                                    List.of("Order does not exist to retrieve.")));

        Hibernate.initialize(order.getPayments());
        Hibernate.initialize(order.getItems());

        return Optional.of(order);
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Optional<Order>
    createOrder(OrderCreateRequest request, HttpServletRequest servletRequest) {
        Profile profile = profileRepository.findMember(request.getUserId(), UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid profile",
                                                    List.of("Normal profile does not exist.")));

        List<Product> products = productRepository
                .findProducts(request.getItems()
                                      .stream()
                                      .map(CartItemNested::getProductId)
                                      .toList(),
                              ProductStatus.ENABLED,
                              ProductStockStatus.IN_STOCK,
                              false);

        RequestHeaderParser requestHeaderParser = this.requestHeaderParser.changeHttpServletRequest(servletRequest);

        Order order = Order.builder(request,
                                    profile,
                                    requestHeaderParser.getIpAddress(),
                                    requestHeaderParser.getUserAgent(),
                                    requestHeaderParser.getAcceptLanguage()).build();

        request.getItems().forEach(cartItemNested -> {
            Product product = products
                    .stream()
                    .filter(p -> Objects.equals(p.getId(), cartItemNested.getProductId()))
                    .findAny()
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                        String.format("productId: %s N/A", cartItemNested.getProductId()),
                                                        List.of("Your cart item is currently out of stock.")));

            OrderItem orderItem = OrderItem.builder(product.getName(),
                                                    product.getSubtitle(),
                                                    product.getSlug(),
                                                    product.getPrice(),
                                                    cartItemNested.getQuantity()).build();

            order.add(orderItem);
        });

        orderRepository.save(order); // orderItems persisted in cascaded.

        return Optional.of(order);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public boolean deleteOrder(Long orderId, Long userId) {
        return orderRepository.findOrder(orderId,
                                         userId,
                                         null,
                                         null,
                                         null,
                                         null,
                                         null,
                                         null,
                                         null,
                                         null,
                                         true)
                .map(order -> {
                    orderRepository.delete(order);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Soft removed order not found",
                                                      List.of("Order does not exist to delete.")));
    }
}
