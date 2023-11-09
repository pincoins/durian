package kr.pincoin.durian.shop.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.DocumentVerification;
import kr.pincoin.durian.auth.domain.PhoneVerification;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.shop.controller.dto.OrderCreateRequest;
import kr.pincoin.durian.shop.domain.*;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.domain.conveter.PaymentAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
@Slf4j
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findOrder() {
        init();

        List<Order> orders = orderRepository.findOrders(null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null);

        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    void findOrderWithPayments() {
        init();

        Order order = orderRepository.findOrders(null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null,
                                                  null).get(0);

        Optional<Order> order1 = orderRepository.findOrderWithPayments(order.getId(),
                                                                      order.getUser().getId(),
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null);
        assertThat(order1).isPresent();
        assertThat(order1.get().getPayments().get(0).getAmount()).isEqualTo(new BigDecimal("94250.00"));
        assertThat(order1.get().getTotalPaidAmount().setScale(0, RoundingMode.DOWN))
                .isEqualTo(new BigDecimal(188500));
    }

    @Test
    void findOrderItemsWithVouchers() {
        init();

        Order order = orderRepository.findOrders(null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 null).get(0);

        List<OrderItem> items = orderItemRepository.findOrderItemsWithVouchers(order.getId(),
                                                                       order.getUser().getId(),
                                                                       null,
                                                                       null,
                                                                       null);

        items.forEach(i -> i.getVouchers().forEach(v -> log.info("{} {}", v.getCode(), v.getRemarks())));
    }

    private void init() {
        User user = User.builder("username",
                                 "password",
                                 "john",
                                 "test@example.com")
                .status(UserStatus.NORMAL)
                .role(Role.MEMBER)
                .build();

        Profile profile = Profile.builder(VerificationStatus.UNVERIFIED,
                                          new PhoneVerification(VerificationStatus.UNVERIFIED),
                                          new DocumentVerification(VerificationStatus.UNVERIFIED)).build();

        profile.belongsTo(user);

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
                                           new StockLevel(100, 200),
                                           0).build();

        Product product2 = Product.builder("product2 slug",
                                           "product2 name",
                                           "product2 subtitle",
                                           "product2 description",
                                           0,
                                           new Price(BigDecimal.valueOf(50000),
                                                     BigDecimal.valueOf(47000),
                                                     BigDecimal.valueOf(46500)),
                                           new StockLevel(100, 200),
                                           0).build();

        category.add(product1);
        category.add(product2);
        categoryRepository.save(category);
        assertThat(category.getTitle()).isEqualTo("category title");
        assertThat(product1.getPrice().getSellingPrice()).isEqualTo(BigDecimal.valueOf(9500));
        assertThat(product2.getPrice().getListPrice()).isEqualTo(BigDecimal.valueOf(50000));

        OrderItem orderItem1 = OrderItem.builder(product1.getName(),
                                                 product1.getSubtitle(),
                                                 product1.getSlug(),
                                                 product1.getPrice(),
                                                 5).build();

        OrderItem orderItem2 = OrderItem.builder(product2.getName(),
                                                 product2.getSubtitle(),
                                                 product2.getSlug(),
                                                 product2.getPrice(),
                                                 3).build();

        OrderItemVoucher orderItemVoucher1 = OrderItemVoucher.builder("1111", "").build();
        OrderItemVoucher orderItemVoucher2 = OrderItemVoucher.builder("2222", "").build();
        OrderItemVoucher orderItemVoucher3 = OrderItemVoucher.builder("3333", "").build();
        OrderItemVoucher orderItemVoucher4 = OrderItemVoucher.builder("4444", "").build();

        Arrays.asList(orderItemVoucher1, orderItemVoucher2)
                .forEach(orderItem1::add);
        Arrays.asList(orderItemVoucher3, orderItemVoucher4)
                .forEach(orderItem1::add);

        Order order = Order.builder(mock(OrderCreateRequest.class), profile, mock(HttpServletRequest.class)).build();

        Arrays.asList(orderItem1, orderItem2).forEach(order::add);

        OrderPayment orderPayment1 = OrderPayment.builder(PaymentAccount.KB,
                                                          order.getTotalSellingPrice()
                                                                  .divide(BigDecimal.valueOf(2),
                                                                          0, RoundingMode.HALF_UP),
                                                          BigDecimal.ZERO).build();

        OrderPayment orderPayment2 = OrderPayment.builder(PaymentAccount.KB,
                                                          order.getTotalSellingPrice()
                                                                  .divide(BigDecimal.valueOf(2),
                                                                          0, RoundingMode.HALF_UP),
                                                          BigDecimal.ZERO).build();

        Arrays.asList(orderPayment1, orderPayment2).forEach(order::add);

        orderRepository.save(order);
        assertThat(order.getOrderUuid()).isNotBlank();
        assertThat(order.getTotalListPrice()).isEqualTo(BigDecimal.valueOf(200000));
        assertThat(order.getTotalSellingPrice()).isEqualTo(BigDecimal.valueOf(188500));
        assertThat(orderPayment1.getAmount().add(orderPayment2.getAmount())).isEqualTo(BigDecimal.valueOf(188500));

        em.flush();
        em.clear();
    }
}