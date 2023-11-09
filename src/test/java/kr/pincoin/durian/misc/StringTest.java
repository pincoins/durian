package kr.pincoin.durian.misc;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StringTest {
    @Test
    void renameFile() {
        String filename = "test1234.png";
        String filename1 = ".gitignore";
        String filename2 = "test";
        String filename3 = "test.test.png";

        System.out.printf("shop/%s/%s.%s%n",
                          LocalDateTime.now().format(DateTimeFormatter.ISO_DATE), // 2023-01-01
                          UUID.randomUUID(), // uuid4
                          filename.substring(filename.lastIndexOf(".") + 1)); // file extension

        assertThat(filename.lastIndexOf('.')).isGreaterThan(0); // 8
        assertThat(filename1.lastIndexOf('.')).isEqualTo(0); // 0
        assertThat(filename2.lastIndexOf('.')).isLessThan(0); // -1
        assertThat(filename3.lastIndexOf('.')).isGreaterThan(0); // 9
    }
}
