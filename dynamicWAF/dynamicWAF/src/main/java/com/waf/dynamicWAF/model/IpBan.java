package com.waf.dynamicWAF.model;

// fact, not event
public class IpBan {
    private String ipAddress;
    private String reason;

    public IpBan(String ipAddress, String reason) {
        this.ipAddress = ipAddress;
        this.reason = reason;
    }

    // Getteri i Setteri
    public String getIpAddress() { return ipAddress; }
    public String getReason() { return reason; }
}