package com.waf.dynamicWAF.controller;

import com.waf.dynamicWAF.model.IpBan;
import com.waf.dynamicWAF.model.RequestEvent;
import com.waf.dynamicWAF.service.WafCepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/waf")
public class WafCepController {

    private final WafCepService wafCepService;

    public WafCepController(WafCepService wafCepService) {
        this.wafCepService = wafCepService;
    }

    /**
     * Endpoint za slanje sumnjivog HTTP zahteva.
     */
    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeRequest(
            @RequestParam String ip,
            @RequestParam(defaultValue = "GET") String method,
            @RequestParam(defaultValue = "/") String uri,
            @RequestParam(required = false) String payload,
            @RequestParam(required = false) String queryKey,
            @RequestParam(required = false) String queryValue) {

        Map<String, String> params = new HashMap<>();
        if (queryKey != null && queryValue != null) {
            params.put(queryKey, queryValue);
        }

        // Kreiramo pravi događaj sa praznim headerima
        RequestEvent event = new RequestEvent(ip, method, uri, new HashMap<>(), params, payload);

        wafCepService.processRequest(event);
        return ResponseEntity.ok("Zahtev poslat u Drools na analizu za IP: " + ip);
    }

    /**
     * Ključni endpoint za simulaciju napada!
     */
    @PostMapping("/simulate-attack")
    public ResponseEntity<String> simulateAttack(@RequestParam String ip) {
        System.out.println("--- ZAPOČINJEM SIMULACIJU BRUTE FORCE NAPADA ---");

        for (int i = 1; i <= 6; i++) {
            // Simuliramo POST zahteve na /login endpoint
            RequestEvent event = new RequestEvent(ip, "POST", "/login", new HashMap<>(), new HashMap<>(), null);
            wafCepService.processRequest(event);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return ResponseEntity.ok("Simulacija napada završena za IP: " + ip + ". Proveri Dashboard!");
    }

    @GetMapping("/banned-ips")
    public ResponseEntity<List<IpBan>> getBannedIps() {
        List<IpBan> bannedIps = wafCepService.getBannedIps();
        return ResponseEntity.ok(bannedIps);
    }

    /**
     * Endpoint za dinamicko dodavanje pravila u letu preko Drools Templates-a
     */
    @PostMapping("/add-rule")
    public ResponseEntity<String> addDynamicRule(
            @RequestParam String keyword,
            @RequestParam String threatName,
            @RequestParam(defaultValue = "block request") String action) {

        wafCepService.addDynamicRuleFromTemplate(keyword, threatName, action);
        return ResponseEntity.ok("Uspesno dodato dinamicko pravilo za kljucnu rec: " + keyword);
    }
}