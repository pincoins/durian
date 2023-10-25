package kr.pincoin.durian.auth.repository.jpa;


import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void addUser() {
        User user = new User("username",
                             "password",
                             "john",
                             "test@example.com",
                             UserStatus.NORMAL);

        assertThat(user.getId()).isNull(); // `user` entity is new/transient.

        userRepository.save(user);

        assertThat(user.getId()).isPositive(); // `user` entity is now being managed.
    }

    // check duplicate username

    // check duplicate email address

    // validate username pattern

    // validate password pattern

    // change password

    // inactivate user

    // delete user
}