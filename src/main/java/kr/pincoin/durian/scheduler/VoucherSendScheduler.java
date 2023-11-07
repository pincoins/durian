package kr.pincoin.durian.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class VoucherSendScheduler {
    // PAID & NOT_SENT & VERIFIED & 10 minutes past

    @Scheduled(fixedRateString = "${pincoin.scheduler.voucher-send}", initialDelay = 3 * 1000) // autostart
    public void run() {
        log.warn("send vouchers: {}", LocalDateTime.now());
    }
}
