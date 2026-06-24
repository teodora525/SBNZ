package com.waf.dynamicWAF.model;

import lombok.Data;

@Data
public class ThreatScore {
    private String ipAddress;
    private int score;

    public ThreatScore(String ipAddress) {
        this.ipAddress = ipAddress;
        this.score = 0;
    }

    // Metoda za dodavanje poena
    public void increment(int points) {
        this.score += points;
    }
}