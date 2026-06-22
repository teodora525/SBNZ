package com.waf.dynamicWAF.service;

import com.waf.dynamicWAF.model.IpBan;
import com.waf.dynamicWAF.model.ThreatAlert;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.List;

@Service
public class WafCepService {

    private final KieContainer kieContainer;
    private KieSession kieSession;

    public WafCepService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @PostConstruct
    public void init() {
        kieSession = kieContainer.newKieSession("cepKsession");

        if (kieSession == null) {
            throw new RuntimeException("Kritična greška: Drools nije uspeo da pronađe 'cepKsession'! Uradi Maven Clean Install.");
        }

        System.out.println("CEP Drools sesija je uspešno inicijalizovana u STREAM modu.");
    }

    public void receiveSuspiciousRequest(String ipAddress, String type) {
        ThreatAlert alert = new ThreatAlert(ipAddress, type);
        kieSession.insert(alert);
        int firedRules = kieSession.fireAllRules();
        System.out.println("Zabeležen zahtev sa IP: " + ipAddress + " | Okinuto pravila: " + firedRules);
    }

    // --- NOVO: Metoda za izvlačenje banovanih adresa iz Drools-a ---
    public List<IpBan> getBannedIps() {
        List<IpBan> banovaneAdrese = new ArrayList<>();

        // Pozivamo tačan naziv upita iz cep-rules.drl fajla
        QueryResults rezultati = kieSession.getQueryResults("pribaviSveBanovaneIpAdrese");

        // Prolazimo kroz rezultate
        for (QueryResultsRow red : rezultati) {
            // "$ban" je naziv varijable iz .drl fajla
            IpBan ban = (IpBan) red.get("$ban");
            banovaneAdrese.add(ban);
        }

        return banovaneAdrese;
    }

    @PreDestroy
    public void cleanup() {
        if (kieSession != null) {
            kieSession.dispose();
        }
    }
}