package kr.pincoin.durian.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class StockAlertScheduler {
    @Scheduled(cron = "${pincoin.scheduler.stock-alert}")
    public void run() {
        log.warn("stock level update & alert: {}", LocalDateTime.now());
    }
}
