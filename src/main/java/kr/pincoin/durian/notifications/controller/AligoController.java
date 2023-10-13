package kr.pincoin.durian.notifications.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.notifications.dto.AligoSendRequest;
import kr.pincoin.durian.notifications.dto.AligoSendResponse;
import kr.pincoin.durian.notifications.service.AligoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aligo")
@CrossOrigin("*")
@Slf4j
public class AligoController {
    private final AligoService aligoService;

    public AligoController(AligoService aligoService) {
        this.aligoService = aligoService;
    }

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
