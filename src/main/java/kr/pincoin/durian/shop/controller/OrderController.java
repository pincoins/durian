package kr.pincoin.durian.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.OrderCreateRequest;
import kr.pincoin.durian.shop.controller.dto.OrderResponse;
import kr.pincoin.durian.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

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
}
