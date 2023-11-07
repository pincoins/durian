package kr.pincoin.durian.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.pincoin.durian.auth.service.IdentityService;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.*;
import kr.pincoin.durian.shop.domain.conveter.*;
import kr.pincoin.durian.shop.service.OrderItemService;
import kr.pincoin.durian.shop.service.OrderService;
import kr.pincoin.durian.shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    private final OrderItemService orderItemService;

    private final PaymentService paymentService;

    private final IdentityService identityService;

    @GetMapping("")
    public ResponseEntity<List<? extends OrderResponse>>
    orderList(@RequestParam(name = "userId", required = false) Long userId,
              @RequestParam(name = "status", required = false) OrderStatus status,
              @RequestParam(name = "paymentMethod", required = false) PaymentMethod paymentMethod,
              @RequestParam(name = "payment", required = false) PaymentStatus payment,
              @RequestParam(name = "sending", required = false) SendingStatus sending,
              @RequestParam(name = "visibility", required = false) OrderVisibility visibility,
              @RequestParam(name = "fullName", required = false) String fullName,
              @RequestParam(name = "orderUuid", required = false) String orderUuid,
              @RequestParam(name = "transactionId", required = false) String transactionId,
              @RequestParam(name = "removed", required = false) Boolean removed,
              @AuthenticationPrincipal UserDetails userDetails) {
        // order list does not have to list items, payments and sent vouchers.
        return ResponseEntity
                .ok()
                .body(orderService.listOrders(userId,
                                              status,
                                              paymentMethod, payment,
                                              sending,
                                              visibility,
                                              fullName,
                                              orderUuid,
                                              transactionId,
                                              removed)
                              .stream()
                              .map(order -> identityService.isAdmin(userDetails)
                                      ? new OrderAdminResponse(order)
                                      : new OrderResponse(order))
                              .toList());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<? extends OrderResponse>
    orderDetail(@PathVariable Long orderId,
                @RequestParam(name = "userId", required = false) Long userId,
                @RequestParam(name = "status", required = false) OrderStatus status,
                @RequestParam(name = "paymentMethod", required = false) PaymentMethod paymentMethod,
                @RequestParam(name = "payment", required = false) PaymentStatus payment,
                @RequestParam(name = "sending", required = false) SendingStatus sending,
                @RequestParam(name = "visibility", required = false) OrderVisibility visibility,
                @RequestParam(name = "fullName", required = false) String fullName,
                @RequestParam(name = "orderUuid", required = false) String orderUuid,
                @RequestParam(name = "transactionId", required = false) String transactionId,
                @RequestParam(name = "removed", required = false) Boolean removed,
                @AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getOrder(orderId,
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
                .map(order -> ResponseEntity
                        .ok()
                        .body(identityService.isAdmin(userDetails)
                                      ? new OrderItemPaymentAdminResponse(order)
                                      : new OrderItemPaymentResponse(order)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Order not found",
                                                    List.of("Order does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<OrderResponse>
    orderCreate(@Valid @RequestBody OrderCreateRequest request,
                HttpServletRequest servletRequest) {
        return orderService.createOrder(request, servletRequest)
                .map(order -> ResponseEntity.ok().body(new OrderResponse(order)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Order creation failure",
                                                    List.of("Failed to create a new order.")));
    }

    @DeleteMapping("/{orderId}/users/{userId}")
    public ResponseEntity<Object>
    orderDelete(@PathVariable Long orderId,
                @PathVariable Long userId) {
        if (orderService.deleteOrder(orderId, userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{orderId}/users/{userId}/payments")
    public ResponseEntity<List<OrderPaymentResponse>>
    orderListPayments(@PathVariable Long orderId,
                      @PathVariable Long userId,
                      @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .ok()
                .body(paymentService.listOrderPayments(orderId, userId, userDetails)
                              .stream()
                              .map(OrderPaymentResponse::new)
                              .toList());
    }

    @GetMapping("/{orderId}/users/{userId}/items")
    public ResponseEntity<List<OrderItemResponse>>
    orderListItems(@PathVariable Long orderId,
                   @PathVariable Long userId,
                   @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .ok()
                .body(orderItemService.listOrderItems(orderId, userId, userDetails)
                              .stream()
                              .map(OrderItemResponse::new)
                              .toList());
    }

    @GetMapping("/{orderId}/users/{userId}/items/{itemId}/vouchers")
    public ResponseEntity<List<OrderItemVoucherResponse>>
    orderListItemVouchers(@PathVariable Long orderId,
                          @PathVariable Long userId,
                          @PathVariable Long itemId,
                          @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .ok()
                .body(orderItemService.listOrderItemVouchers(orderId, userId, itemId, userDetails)
                              .stream()
                              .map(OrderItemVoucherResponse::new)
                              .toList());
    }
}
