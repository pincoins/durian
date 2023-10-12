package kr.pincoin.durian.notifications.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.pincoin.durian.notifications.dto.AligoSendRequest;
import kr.pincoin.durian.notifications.dto.AligoSendResponse;
import kr.pincoin.durian.notifications.dto.AligoSendResult;
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
public class AligoService {
    @Value("${aligo.api-key}")
    private String apiKey;

    @Value("${aligo.user-id}")
    private String userId;

    @Value("${aligo.sender}")
    private String sender;

    private final WebClient webClient;

    public AligoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://apis.aligo.in").build(); // for production
    }

    @Transactional
    public Optional<AligoSendResponse> send(AligoSendRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("key", apiKey);
        formData.add("user_id", userId);
        formData.add("sender", sender);
        formData.add("receiver", request.getPhone());
        formData.add("msg", request.getMessage());

        // Caution:
        // rest-api endpoint must contain trailing "/". Otherwise, 301 redirect
        // Accept header is "text/html" not "application/json", so parse json with string

        String jsonString = webClient.post()
                .uri("/send/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_HTML)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            AligoSendResult result = mapper.readValue(jsonString, AligoSendResult.class);

            if (result.getResultCode().equals("1") && result.getMessage().equals("success")) {
                return Optional.of(new AligoSendResponse(result.getResultCode(),
                                                         result.getMessage(),
                                                         result.getMsgId(),
                                                         result.getSuccessCnt(),
                                                         result.getErrorCnt(),
                                                         result.getMsgType()));
            }

            log.warn("failed to send short text message: {} {}", result.getResultCode(), result.getMessage());
        } catch (JsonProcessingException ignored) {
        }

        return Optional.empty();
    }
}
