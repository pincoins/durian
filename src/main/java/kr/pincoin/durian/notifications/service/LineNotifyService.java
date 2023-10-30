package kr.pincoin.durian.notifications.service;

import kr.pincoin.durian.notifications.controller.dto.LineNotifyRequest;
import kr.pincoin.durian.notifications.controller.dto.LineNotifyResponse;
import kr.pincoin.durian.notifications.service.dto.LineNotifyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@Slf4j
public class LineNotifyService {
    @Value("${line-notify.token}")
    private String token;

    private final WebClient webClient;

    public LineNotifyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://notify-api.line.me/api").build(); // for production
    }

    @Transactional
    public Optional<LineNotifyResponse> send(LineNotifyRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("message", request.getMessage());

        LineNotifyResult result = webClient.post()
                .uri("/notify")
                .headers(header -> header.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(LineNotifyResult.class)
                .block();

        if (result != null) {
            if (result.getStatus().equals("200")) {
                return Optional.of(new LineNotifyResponse(result.getStatus(), result.getMessage()));
            }

            log.warn("failed to send message through line-notify: {} {}", result.getStatus(), result.getMessage());
        }

        return Optional.empty();
    }
}
