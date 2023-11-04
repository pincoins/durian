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
import kr.pincoin.durian.shop.domain.*;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.domain.conveter.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
@Slf4j
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

        Category category = Category.builder("category title",
                                             "category slug",
                                             "category description",
                                             "category sub description",
                                             BigDecimal.valueOf(7.25),
                                             CategoryStatus.NORMAL).build();



        Product product1 = Product.builder("product1 slug",
                                           "product1 name",
                                           "product1 subtitle",
                                           "product1 description",
                                           0,
                                           new Price(BigDecimal.valueOf(10000),
                                                     BigDecimal.valueOf(9500),
                                                     BigDecimal.valueOf(9400)),
                                           new StockLevel(100, 200))
                .build();

        Product product2 = Product.builder("product2 slug",
                                           "product2 name",
                                           "product2 subtitle",
                                           "product2 description",
                                           0,
                                           new Price(BigDecimal.valueOf(50000),
                                                     BigDecimal.valueOf(47000),
                                                     BigDecimal.valueOf(46500)),
                                           new StockLevel(100, 200))
                .build();

        category.addProduct(product1);
        category.addProduct(product2);
        categoryRepository.save(category);
        assertThat(category.getTitle()).isEqualTo("category title");
        assertThat(product1.getPrice().getSellingPrice()).isEqualTo(BigDecimal.valueOf(9500));
        assertThat(product2.getPrice().getListPrice()).isEqualTo(BigDecimal.valueOf(50000));

        Order order = Order.builder(PaymentMethod.BANK_TRANSFER, profile, mock(HttpServletRequest.class)).build();

        orderRepository.save(order);
        assertThat(order.getOrderUuid()).isNotBlank();
        assertThat(order.getTotalListPrice()).isEqualTo(profile.getTotalListPrice());
        assertThat(order.getTotalSellingPrice()).isEqualTo(profile.getTotalSellingPrice());
    }
}