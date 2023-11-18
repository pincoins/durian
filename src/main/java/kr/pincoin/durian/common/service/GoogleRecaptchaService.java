package kr.pincoin.durian.common.service;

import jakarta.validation.constraints.NotBlank;
import kr.pincoin.durian.common.service.dto.GoogleRecaptchaResult;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class GoogleRecaptchaService {
    private final WebClient webClient;

    @Value("${google-recaptcha.secret-key}")
    private String secretKey;

    @Value("${google-recaptcha.enabled}")
    private Boolean enabled;

    public GoogleRecaptchaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.google.com/recaptcha/api").build();
    }

    public boolean isUnverified(@NonNull @NotBlank String captcha) {
        if (!enabled) {
            return true;
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("secret", secretKey);
        formData.add("response", captcha);

        GoogleRecaptchaResult result = webClient.post()
                .uri("/siteverify")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(GoogleRecaptchaResult.class)
                .block();

        if (result != null && result.getSuccess()) {
            log.debug("google recaptcha code verified: {} {}", result.getChallengeTs(), result.getHostname());
            return false;
        }

        return true;
    }
}
