package kr.pincoin.durian.shop.controller;

import kr.pincoin.durian.shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

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
