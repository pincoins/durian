package kr.pincoin.durian;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Cron expression scheduler enabled
@RequiredArgsConstructor
@Slf4j
public class DurianApplication {
	public static void main(String[] args) {
		SpringApplication.run(DurianApplication.class, args);
	}
}
