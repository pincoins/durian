package kr.pincoin.durian.notifications.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.notifications.controller.dto.MailgunSendRequest;
import kr.pincoin.durian.notifications.controller.dto.MailgunSendResponse;
import kr.pincoin.durian.notifications.service.MailgunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mailgun")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class MailgunController {
    private final MailgunService mailgunService;

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public ResponseEntity<MailgunSendResponse>
    send(@Valid @RequestBody MailgunSendRequest request) {
        return mailgunService.send(request)
                .map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Failed to send email",
                                                    List.of("Please, contact to administrator.")));
    }
}
