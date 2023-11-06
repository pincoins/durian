package kr.pincoin.durian.shop.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.OrderPaymentCreateRequest;
import kr.pincoin.durian.shop.controller.dto.OrderPaymentResponse;
import kr.pincoin.durian.shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<OrderPaymentResponse>
    paymentAdd(@Valid @RequestBody OrderPaymentCreateRequest request) {
        return paymentService.addPayment(request)
                .map(orderPayment -> ResponseEntity.ok().body(new OrderPaymentResponse(orderPayment)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Payment addition failure",
                                                    List.of("Failed to add a payment.")));
    }

    @PostMapping("/${pincoin.request-mapping.bank-callback}")
    public ResponseEntity<String>
    callbackBank(@RequestParam(name = "account", required = false) String account,
                 @RequestParam(name = "received", required = false) String received,
                 @RequestParam(name = "name", required = false) String name,
                 @RequestParam(name = "method", required = false) String method,
                 @RequestParam(name = "amount", required = false) String amount,
                 @RequestParam(name = "balance", required = false) String balance) {
        return ResponseEntity
                .ok()
                .body(paymentService.addPayment(account, received, name, method, amount, balance)
                              ? "ok"
                              : "fail");
    }

    @PostMapping("/${pincoin.request-mapping.paypal-callback}")
    public String
    callbackPaypal() {
        return "paypal";
    }

    @PostMapping("/${pincoin.request-mapping.billgate-callback}")
    public String
    callbackBillgate() {
        return "billgate";
    }
}
