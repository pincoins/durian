package kr.pincoin.durian.shop.domain.conveter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PaymentAccountTest {

    @Test
    public void enumTest() {
        Assertions.assertThat(PaymentAccount.KB.getCode()).isEqualTo("0");
        Assertions.assertThat(PaymentAccount.fromCode("0")).isEqualTo(PaymentAccount.KB);
    }

}