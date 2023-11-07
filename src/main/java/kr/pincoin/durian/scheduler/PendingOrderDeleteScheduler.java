package kr.pincoin.durian.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class PendingOrderDeleteScheduler {
    @Scheduled(cron = "${pincoin.scheduler.pending-order-delete}")
    public void run() {
        log.warn("pending order delete: {}", LocalDateTime.now());
    }
}
