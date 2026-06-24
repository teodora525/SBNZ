package com.waf.dynamicWAF.runner;

import com.waf.dynamicWAF.model.RequestEvent;
import com.waf.dynamicWAF.model.SystemContext;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.HashMap;
import java.util.Map;

//@Component
public class WafTestRunner implements CommandLineRunner {

    @Autowired
    private KieContainer kieContainer;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=========================================");
        System.out.println("POKRETANJE DROOLS WAF SISTEMA (3. DOMAĆI)");
        System.out.println("=========================================\n");

        // 1. Kreiranje sesije
        KieSession kieSession = kieContainer.newKieSession();

        // 2. Ubacivanje trenutnog konteksta sistema u radnu memoriju
        kieSession.insert(SystemContext.NORMAL_TRAFFIC);

        // 3. Kreiranje testnih HTTP zahteva
        // - Zahtev 1: Legitimni korisnik
        Map<String, String> normalQueryParams = new HashMap<>();
        RequestEvent normalReq = new RequestEvent("192.168.1.5", "GET", "/api/users", new java.util.HashMap<>(), normalQueryParams, "{}");
        // Zahtev 2: Pokušaj SQL Injection-a
        Map<String, String> sqlQueryParams = new HashMap<>();
        RequestEvent sqlReq = new RequestEvent("203.0.113.42", "POST", "/api/login", new HashMap<>(), sqlQueryParams, "{\"username\": \"admin\", \"password\": \"' UNION SELECT * FROM users\"}");

// Zahtev 3: Pokušaj XSS-a
        Map<String, String> xssQueryParams = new HashMap<>();
        RequestEvent xssReq = new RequestEvent("198.51.100.7", "POST", "/api/comments", new HashMap<>(), xssQueryParams, "<script>alert('XSS')</script>");
        kieSession.insert(normalReq);
        kieSession.insert(sqlReq);
        kieSession.insert(xssReq);

        // 5. Okidanje pravila
        int firedRules = kieSession.fireAllRules();

        // 6. Oslobađanje memorije
        kieSession.dispose();

        System.out.println("\n=========================================");
        System.out.println("UKUPNO TRIGEGROVANO PRAVILA: " + firedRules);
        System.out.println("=========================================");
    }
}