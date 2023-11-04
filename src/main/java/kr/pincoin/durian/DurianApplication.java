package kr.pincoin.durian;

import jakarta.annotation.PostConstruct;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling // Cron expression scheduler enabled
@RequiredArgsConstructor
@Slf4j
public class DurianApplication {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DurianApplication.class, args);
	}

	@PostConstruct
	public void init() {
		if (userRepository.findAdmins(null).isEmpty()) {
			User admin = User.builder("admin",
									  passwordEncoder.encode("admin"),
									  "administrator",
									  "admin@example.com")
					.status(UserStatus.NORMAL)
					.role(Role.SYSADMIN)
					.build();

			userRepository.save(admin);

			log.warn("System administrator was added. Password must be reset.");
		}
	}
}
