package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.dto.UserCreateRequest;
import kr.pincoin.durian.auth.dto.UserResponse;
import kr.pincoin.durian.auth.jwt.TokenProvider;
import kr.pincoin.durian.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       TokenProvider tokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse
    createUser(UserCreateRequest request) {
        User user = userRepository.save(new User(request.getUsername(),
                                                 passwordEncoder.encode(request.getPassword()),
                                                 request.getName(),
                                                 request.getEmail()).activate());

        return new UserResponse(user);
    }
}
