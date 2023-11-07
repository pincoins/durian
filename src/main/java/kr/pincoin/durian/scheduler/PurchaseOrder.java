package kr.pincoin.durian.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class PurchaseOrder {
    @Scheduled(cron = "${pincoin.scheduler.purchase-order}")
    public void run() {
        log.warn("purchase order: {}", LocalDateTime.now());
    }
}
