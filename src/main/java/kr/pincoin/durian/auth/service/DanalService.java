package kr.pincoin.durian.auth.service;

import kr.pincoin.durian.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class DanalService {
    private final WebClient webClient;

    @Value("${danal.cp-id}")
    private String cpId;

    @Value("${danal.cp-pwd}")
    private String cpPwd;

    @Value("${danal.cp-title}")
    private String cpTitle;

    @Value("${danal.target-url}")
    private String targetUrl;

    public DanalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://uas.teledit.com").build(); // for production
    }

    public Optional<String> callItemSend(Long userId) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("TXTYPE", "ITEMSEND");
        formData.add("SERVICE", "UAS");
        formData.add("AUTHTYPE", "36");
        formData.add("CPID", cpId);
        formData.add("CPPWD", cpPwd);
        formData.add("CPTITLE", cpTitle);
        formData.add("TARGETURL", String.format(targetUrl, userId)); // http://localhost:8080/members/%s/danal
        formData.add("USERID", String.valueOf(userId));
        formData.add("CHARSET", "EUC-KR");

        String result = webClient.post()
                .uri("/uas")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (result == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                                   "Invalid data",
                                   List.of("Failed to retrieve TID from danal."));
        }

        Map<String, String> map = new HashMap<>();

        Arrays.stream(result.split("&")).forEach(s -> {
            int i = s.indexOf('=');

            if (i > 0) {
                map.put(s.substring(0, i).trim(), s.substring(i + 1).trim());
            }
        });

        if (map.containsKey("TID")) {
            return Optional.of(map.get("TID"));
        }

        throw new ApiException(HttpStatus.BAD_REQUEST,
                               "TID not found",
                               List.of("Failed to retrieve TID from danal."));
    }
}
