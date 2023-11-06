package kr.pincoin.durian.shop.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    @PostMapping("/${pincoin.request-mapping.bank-callback}")
    public String callbackBank(@RequestParam String account,
                               @RequestParam String received,
                               @RequestParam String name,
                               @RequestParam String method,
                               @RequestParam String amount,
                               @RequestParam String balance) {
        log.warn("{} {} {} {} {} {}", account, received, name, method, amount, balance);
        return "bank";
    }

    @PostMapping("/${pincoin.request-mapping.paypal-callback}")
    public String callbackPaypal() {
        return "paypal";
    }

    @PostMapping("/${pincoin.request-mapping.billgate-callback}")
    public String callbackBillgate() {
        return "billgate";
    }

    @Data
    public static class Dto {
        String received;
    }
}
