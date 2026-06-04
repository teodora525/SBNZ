package com.waf.dynamicWAF.service;

import com.waf.dynamicWAF.model.ThreatAlert;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class WafCepService {

    private final KieContainer kieContainer;
    private KieSession kieSession;

    public WafCepService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @PostConstruct
    public void init() {
        // Više nam ne trebaju dugačke konfiguracije.
        // newKieSession() sada automatski vuče STREAM mod iz kmodule.xml-a!
        this.kieSession = kieContainer.newKieSession();
        System.out.println("CEP Drools sesija je uspešno inicijalizovana u STREAM modu.");
    }

    public void receiveSuspiciousRequest(String ipAddress, String type) {
        ThreatAlert alert = new ThreatAlert(ipAddress, type);
        kieSession.insert(alert);
        int firedRules = kieSession.fireAllRules();
        System.out.println("Zabeležen zahtev sa IP: " + ipAddress + " | Okinuto pravila: " + firedRules);
    }

    @PreDestroy
    public void cleanup() {
        if (kieSession != null) {
            kieSession.dispose();
        }
    }
}