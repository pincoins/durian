package kr.pincoin.durian.auth.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class UserManageScheduler {
    @Scheduled(cron = "${pincoin.scheduler.auth.user-manage}")
    public void run() {
        log.info("user inactivate: {}", LocalDateTime.now());

        // 1. limit customers who stopped buying

        // 2. revoke allow-order

        // 3. revoke phone verifications

        // 4. revoke document verifications

        // 5. revoke email verifications

        // 6. inactivate users

        // 7. remove unverified accounts

        // 8. delete users
    }
}
