package kr.pincoin.durian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing // created, modified fields = Django compatible
@EnableScheduling // crontab style scheduler enabled
public class DurianApplication {

	public static void main(String[] args) {
		SpringApplication.run(DurianApplication.class, args);
	}

}
