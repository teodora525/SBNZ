package com.waf.dynamicWAF.model;

import lombok.Data;

@Data
public class WafConfiguration {
    private SystemContext currentContext = SystemContext.NORMAL_TRAFFIC;

    // Ovo su parametri C[K] iz tvoje specifikacije koji se dinamički menjaju
    private int maxAllowedScore;
    private int maxRequestsPerMinute;
    private int maxFailedLoginsPerMinute;
    private int maxBannedIpsPerMinute;

    public WafConfiguration() {
        setContextAndThresholds(SystemContext.NORMAL_TRAFFIC); // Default na početku
    }

    // Kada se promeni kontekst, pragovi se automatski zatežu ili popuštaju
    public void setContextAndThresholds(SystemContext context) {
        this.currentContext = context;
        switch (context) {
            case NORMAL_TRAFFIC:
                this.maxAllowedScore = 100;
                this.maxRequestsPerMinute = 100;
                this.maxFailedLoginsPerMinute = 5;
                this.maxBannedIpsPerMinute = 10;
                break;
            case UNDER_ATTACK:
                this.maxAllowedScore = 50;  // Duplo strožije
                this.maxRequestsPerMinute = 50;
                this.maxFailedLoginsPerMinute = 3;
                this.maxBannedIpsPerMinute = 5;
                break;
            case ZERO_TRUST:
                this.maxAllowedScore = 10;  // Ekstremno strogo, nema opraštanja
                this.maxRequestsPerMinute = 20;
                this.maxFailedLoginsPerMinute = 1;
                this.maxBannedIpsPerMinute = 1;
                break;
            case MAINTENANCE:
                this.maxAllowedScore = 50;
                this.maxRequestsPerMinute = 50;
                this.maxFailedLoginsPerMinute = 5;
                this.maxBannedIpsPerMinute = 10;
                break;
        }
    }
}