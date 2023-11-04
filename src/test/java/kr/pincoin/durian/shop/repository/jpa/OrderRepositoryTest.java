package kr.pincoin.durian.shop.repository.jpa;

import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.DocumentVerification;
import kr.pincoin.durian.auth.domain.PhoneVerification;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
@Slf4j
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void createNewOrder() {
        User user = User.builder("username",
                                 "password",
                                 "john",
                                 "test@example.com")
                .status(UserStatus.NORMAL)
                .role(Role.MEMBER)
                .build();

        Profile profile = Profile.builder(user,
                                          VerificationStatus.UNVERIFIED,
                                          new PhoneVerification(VerificationStatus.UNVERIFIED),
                                          new DocumentVerification(VerificationStatus.UNVERIFIED))
                .build();

        profileRepository.save(profile);
        assertThat(profile).isNotNull();

        Order order = Order.builder(PaymentMethod.BANK_TRANSFER, profile, mock(HttpServletRequest.class)).build();

        orderRepository.save(order);

        assertThat(order.getOrderUuid()).isNotBlank();
        assertThat(order.getTotalListPrice()).isEqualTo(profile.getTotalListPrice());
        assertThat(order.getTotalSellingPrice()).isEqualTo(profile.getTotalSellingPrice());
    }
}