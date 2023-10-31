package kr.pincoin.durian.notification.service;

import kr.pincoin.durian.notification.controller.dto.MailgunSendRequest;
import kr.pincoin.durian.notification.controller.dto.MailgunSendResponse;
import kr.pincoin.durian.notification.service.dto.MailgunSendResult;
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
public class MailgunService {
    @Value("${mailgun.api-key}")
    private String apiKey;

    private final WebClient webClient;

    public MailgunService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.mailgun.net/v3/mg1.pincoin.co.kr")
                .build(); // for production
    }

    @Transactional
    public Optional<MailgunSendResponse> send(MailgunSendRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("from", request.getFrom());
        formData.add("to", request.getTo());
        formData.add("subject", request.getSubject());
        formData.add("text", request.getText());

        MailgunSendResult result = webClient.post()
                .uri("/messages")
                .headers(header -> header.setBasicAuth("api", apiKey))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(MailgunSendResult.class)
                .block();

        if (result != null) {
            return Optional.of(new MailgunSendResponse(result.getId(), result.getMessage()));
        }

        log.warn("failed to send email");

        return Optional.empty();
    }
}
