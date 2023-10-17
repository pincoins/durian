package kr.pincoin.durian.auth.dto;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserProfileResult {
    private User user;

    private Profile profile;

    public UserProfileResult(User user, Profile profile) {
        this.user = user;
        this.profile = profile;
    }
}
