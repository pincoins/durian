package kr.pincoin.durian.shop.controller;

import kr.pincoin.durian.shop.service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vouchers")
@CrossOrigin("*")
@Slf4j
public class VoucherController {
    private final VoucherService voucherService;
    
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }
}