package com.waf.dynamicWAF.controller;

import com.waf.dynamicWAF.service.WafCepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waf")
public class WafCepController {

    private final WafCepService wafCepService;

    // Injektujemo naš CEP servis
    public WafCepController(WafCepService wafCepService) {
        this.wafCepService = wafCepService;
    }

    /**
     * Endpoint za slanje pojedinačnog sumnjivog zahteva.
     * Primer poziva: POST http://localhost:8080/api/waf/alert?ip=192.168.1.50&type=SQL_INJECTION
     */
    @PostMapping("/alert")
    public ResponseEntity<String> triggerAlert(
            @RequestParam String ip,
            @RequestParam String type) {

        wafCepService.receiveSuspiciousRequest(ip, type);
        return ResponseEntity.ok("Događaj uspešno poslat u Drools engine za IP: " + ip);
    }

    /**
     * Ključni endpoint za odbranu domaćeg!
     * Simulira automatski napad ispaljivanjem 5 zahteva u sekundi sa iste IP adrese.
     * Primer poziva: POST http://localhost:8080/api/waf/simulate-attack?ip=192.168.1.100
     */
    @PostMapping("/simulate-attack")
    public ResponseEntity<String> simulateAttack(@RequestParam String ip) {
        System.out.println("--- ZAPOČINJEM SIMULACIJU BRUTE FORCE NAPADA ---");

        // Ispaljujemo 5 sumnjivih događaja zaredom u kratkom vremenskom roku
        for (int i = 1; i <= 5; i++) {
            wafCepService.receiveSuspiciousRequest(ip, "FAILED_LOGIN");
            try {
                // Mala pauza od 200 milisekundi između zahteva, čisto da simuliramo realan protok
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return ResponseEntity.ok("Simulacija napada završena za IP: " + ip + ". Proveri konzolu za CEP alarm!");
    }
}