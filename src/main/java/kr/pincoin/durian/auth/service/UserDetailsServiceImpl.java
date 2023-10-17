package kr.pincoin.durian.auth.service;


import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User
    loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository
                .findUser(Long.valueOf(id), UserStatus.NORMAL)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
