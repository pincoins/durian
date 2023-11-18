package kr.pincoin.durian.shop.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class StockAlertSchedulerScheduler {
    @Scheduled(cron = "${pincoin.scheduler.shop.stock-alert}")
    public void run() {
        log.info("stock level update & alert: {}", LocalDateTime.now());
    }
}
