package com.waf.dynamicWAF.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class RequestEvent {
    private String ipAddress;
    private String httpMethod;
    private String uri;
    private Map<String, String> queryParams;
    private String payload;
    private LocalDateTime timestamp = LocalDateTime.now();

    private boolean isBlocked = false;
    private String blockReason;

    public RequestEvent(String ipAddress, String httpMethod, String uri, Map<String, String> queryParams, String payload) {
        this.ipAddress = ipAddress;
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.queryParams = queryParams;
        this.payload = payload;
    }
}