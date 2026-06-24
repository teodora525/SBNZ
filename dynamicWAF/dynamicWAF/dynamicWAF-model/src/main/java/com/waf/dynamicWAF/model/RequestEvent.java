package com.waf.dynamicWAF.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

// ISPRAVLJENO: Koristimo staru Date klasu koju Drools razume
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class RequestEvent {
    private String ipAddress;
    private String httpMethod;
    private String uri;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private String payload;

    // ISPRAVLJENO: LocalDateTime zamenjen sa Date
    private Date timestamp = new Date();

    private boolean blocked = false;
    private String blockReason;

    public RequestEvent(String ipAddress, String httpMethod, String uri, Map<String, String> headers, Map<String, String> queryParams, String payload) {
        this.ipAddress = ipAddress;
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.headers = headers;
        this.queryParams = queryParams;
        this.payload = payload;
    }
}