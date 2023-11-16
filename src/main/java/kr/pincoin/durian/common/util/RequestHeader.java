package kr.pincoin.durian.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

public class RequestHeader {
    @Value("${pincoin.real-ip-header}")
    private static String realIpHeader;

    public static String getIpAddress(HttpServletRequest servletRequest) {
        return Optional.ofNullable(servletRequest.getHeader(realIpHeader))
                .orElse(servletRequest.getRemoteAddr());
    }
}
