package kr.pincoin.durian.notifications.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.notifications.controller.dto.LineNotifyRequest;
import kr.pincoin.durian.notifications.controller.dto.LineNotifyResponse;
import kr.pincoin.durian.notifications.service.LineNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/line-notify")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class LineNotifyController {
    private final LineNotifyService lineNotifyService;

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public ResponseEntity<LineNotifyResponse>
    send(@Valid @RequestBody LineNotifyRequest request) {
        return lineNotifyService.send(request)
                .map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Failed to send message through line-notify",
                                                    List.of("Please, contact to administrator.")));
    }
}
