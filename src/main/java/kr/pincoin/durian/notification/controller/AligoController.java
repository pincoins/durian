package kr.pincoin.durian.notification.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.notification.controller.dto.AligoSendRequest;
import kr.pincoin.durian.notification.controller.dto.AligoSendResponse;
import kr.pincoin.durian.notification.service.AligoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aligo")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AligoController {
    private final AligoService aligoService;

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public ResponseEntity<AligoSendResponse>
    send(@Valid @RequestBody AligoSendRequest request) {
        return aligoService.send(request)
                .map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Failed to send short text message",
                                                    List.of("Please, contact to administrator.")));
    }
}
