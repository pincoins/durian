package kr.pincoin.durian.shop.service;

import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.OrderCreateRequest;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.PaymentMethod;
import kr.pincoin.durian.shop.repository.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Transactional
    public Optional<Order>
    createOrder(OrderCreateRequest request, HttpServletRequest servletRequest) {
        String ipAddress = Optional.ofNullable(servletRequest.getHeader("X-Forwarded-For"))
                .orElse(servletRequest.getRemoteAddr());

        String userAgent = Optional.ofNullable(servletRequest.getHeader("User-Agent"))
                .orElse("User-Agent missing");

        String acceptLanguage = Optional.ofNullable(servletRequest.getHeader("Accept-Language"))
                .orElse("Accept-Language missing");

        Profile profile = profileRepository.findMember(request.getUserId(), UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid category",
                                                    List.of("Normal profile does not exist.")));

        Order order = Order.builder(PaymentMethod.BANK_TRANSFER, userAgent, acceptLanguage, ipAddress, profile)
                .build();
        orderRepository.save(order);

        return Optional.of(order);
    }
}
