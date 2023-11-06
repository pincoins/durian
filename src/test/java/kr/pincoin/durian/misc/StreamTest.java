package kr.pincoin.durian.misc;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest {
    @Test
    void distinctCount() {
        List<Long> numbers = new ArrayList<>();

        numbers.add(1L);
        numbers.add(2L);
        numbers.add(3L);
        numbers.add(2L);

        long count = numbers.stream().distinct().count();

        assertThat(count).isEqualTo(3);

        List<User> users = new ArrayList<>();

        users.add(new User(1L, "a", 1000L));
        users.add(new User(2L, "b", 2000L));
        users.add(new User(3L, "c", 2000L));
        users.add(new User(2L, "d", 1000L));
        users.add(new User(3L, "c", 3000L));

        long count1 = users.stream().map(u -> u.id).distinct().count();
        long count2 = users.stream().distinct().count();
        long count3 = users.stream().filter(u -> u.amount == 2000L).map(u -> u.id).distinct().count();

        assertThat(count1).isEqualTo(3);
        assertThat(count2).isEqualTo(5);
        assertThat(count3).isEqualTo(2);
    }

    @Data
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
        private Long amount;
    }
}
