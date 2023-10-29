package kr.pincoin.durian.auth.repository.jpa;


import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void addUser() {
        User user = User.builder("username",
                                 "password",
                                 "john",
                                 "test@example.com")
                .status(UserStatus.NORMAL)
                .role(Role.MEMBER)
                .build();

        // `user` entity is new/transient.
        assertThat(user.getId()).isNull();

        userRepository.save(user);

        // `user` entity is now being managed.
        assertThat(user.getId()).isPositive();

        Optional<User> userFound = userRepository.findUser(user.getId(), UserStatus.NORMAL);

        assertThat(userFound).isPresent();
        assertThat(userFound).hasValue(user);
    }

    // check duplicate username

    // check duplicate email address

    // validate username pattern

    // validate password pattern

    // change password

    @Test
    void inactivateUser() {
        User user = User.builder("username",
                                 "password",
                                 "john",
                                 "test@example.com")
                .status(UserStatus.NORMAL)
                .role(Role.MEMBER)
                .build();

        userRepository.save(user).inactivate();

        // It syncs without `em.flush(); em.clear();`.

        Optional<User> userFound = userRepository.findUser(user.getId(), UserStatus.INACTIVE);

        assertThat(userFound).isPresent();
    }

    // delete user
}