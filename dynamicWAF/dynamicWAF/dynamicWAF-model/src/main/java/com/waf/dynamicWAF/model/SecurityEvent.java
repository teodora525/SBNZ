package com.waf.dynamicWAF.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SecurityEvent {
    private String eventType;
    private String sourceIp;
    private LocalDateTime timestamp = LocalDateTime.now();
}