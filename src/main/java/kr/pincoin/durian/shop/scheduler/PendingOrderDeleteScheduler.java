package kr.pincoin.durian.shop.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class PendingOrderDeleteScheduler {
    @Scheduled(cron = "${pincoin.scheduler.shop.pending-order-delete}")
    public void run() {
        log.info("pending order delete: {}", LocalDateTime.now());
    }
}
