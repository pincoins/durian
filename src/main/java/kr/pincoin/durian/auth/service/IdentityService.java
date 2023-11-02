package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.auth.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("identity")
@Slf4j
public class IdentityService {
    public boolean isOwner(Long userId) {
        User user = getUser();

        if (!user.getId().equals(userId)) {
            log.warn("Requested resource user id mismatch: {} {}", user.getId(), userId);
            return false;
        }

        log.trace("{} is owner", userId);

        return true;
    }

    public boolean isAdmin(UserDetails userDetails) {
        return userDetails != null
                && userDetails.getAuthorities().stream().anyMatch(role -> Arrays.asList("ROLE_SYSADMIN",
                                                                                        "ROLE_STAFF")
                .contains(role.getAuthority()));
    }

    private static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}

