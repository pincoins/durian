package kr.pincoin.durian.shop.controller;

import kr.pincoin.durian.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
}
