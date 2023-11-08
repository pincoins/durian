package kr.pincoin.durian.shop.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class PurchaseOrderScheduler {
    @Scheduled(cron = "${pincoin.scheduler.shop.purchase-order}")
    public void run() {
        log.warn("purchase order: {}", LocalDateTime.now());
    }
}
