package kr.pincoin.durian.misc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class DanalTest {
    private final WebClient webClient;

    @Autowired
    public DanalTest(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://uas.teledit.com").build();
    }

    @Test
    void callTrans() throws Exception {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        // fixed
        formData.add("TXTYPE", "ITEMSEND");
        formData.add("SERVICE", "UAS");
        formData.add("AUTHTYPE", "36");

        // secret
        formData.add("CPID", "CPID");
        formData.add("CPPWD", "CCPWD");
        formData.add("TARGETURL", "http://localhost/callback.jsp");
        formData.add("CPTITLE", "www.pincoin.co.kr");
        formData.add("USERID", "1234");
        formData.add("CHARSET", "EUC-KR");

        String result = webClient.post()
                .uri("/uas")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, String> map = new HashMap<>();

        assert result != null;

        Arrays.stream(result.split("&")).forEach(s -> {
            int i = s.indexOf('=');

            if (i > 0) {
                map.put(s.substring(0, i).trim(), s.substring(i + 1).trim());
            }
        });

        Assertions.assertThat(map.get("RETURNCODE")).isEqualTo("9010");

        // Success
        // RETURNCODE=0000&RETURNMSG=No information&TID=202311101216453794284010

        // Invalid CPID
        // RETURNCODE=9015&RETURNMSG=[인증실패] 업체정보에 이상이 있습니다. 해당 서비스 업체로 문의하여 주십시오.&TID=202311101214131612388011

        // Invalid CPPWD
        // RETURNCODE=9014&RETURNMSG=[인증실패] 업체정보에 이상이 있습니다. 해당 서비스 업체로 문의하여 주십시오.&TID=202311101215346243524010
    }

    @Test
    void parseQueryParams() {
        String params = "RETURNCODE=0000&RETURNMSG=No information&TID=202311101216453794284010";

        Map<String, String> map = new HashMap<>();

        Arrays.stream(params.split("&")).forEach(s -> {
            int i = s.indexOf('=');

            if (i > 0) {
                map.put(s.substring(0, i).trim(), s.substring(i + 1));
            }
        });

        System.out.println(map.get("RETURNCODE"));
    }
}
