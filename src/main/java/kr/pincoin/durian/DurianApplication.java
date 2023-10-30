package kr.pincoin.durian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Cron expression scheduler enabled
public class DurianApplication {
	public static void main(String[] args) {
		SpringApplication.run(DurianApplication.class, args);
	}
}
