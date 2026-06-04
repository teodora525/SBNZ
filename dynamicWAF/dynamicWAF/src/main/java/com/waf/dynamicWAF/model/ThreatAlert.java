package com.waf.dynamicWAF.model;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import java.util.Date;

@Role(Role.Type.EVENT)
@Timestamp("executionTime") // Govori Drools-u koje polje predstavlja vreme dešavanja
public class ThreatAlert {

    private String sourceIp;
    private String alertType; // npr. "FAILED_LOGIN", "SUSPICIOUS_PAYLOAD"
    private Date executionTime;

    public ThreatAlert(String sourceIp, String alertType) {
        this.sourceIp = sourceIp;
        this.alertType = alertType;
        this.executionTime = new Date(); // Vreme kada je događaj kreiran
    }

    // Getteri i Setteri
    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public Date getExecutionTime() { return executionTime; }
    public void setExecutionTime(Date executionTime) { this.executionTime = executionTime; }
}