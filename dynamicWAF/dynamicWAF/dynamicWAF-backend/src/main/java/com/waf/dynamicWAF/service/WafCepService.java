package com.waf.dynamicWAF.service;

import com.waf.dynamicWAF.model.IpBan;
import com.waf.dynamicWAF.model.RequestEvent;
import com.waf.dynamicWAF.model.ThreatScore;
import com.waf.dynamicWAF.model.WafConfiguration;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.stereotype.Service;
import org.drools.core.impl.InternalKnowledgeBase;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.io.ResourceType;
import org.kie.internal.utils.KieHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WafCepService {

    private final KieContainer kieContainer;
    private KieSession kieSession;
    private WafConfiguration wafConfig; // Zadržavamo konfiguraciju

    public WafCepService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @PostConstruct
    public void init() {
        kieSession = kieContainer.newKieSession("cepKsession");

        if (kieSession == null) {
            throw new RuntimeException("Kritična greška: Drools nije uspeo da pronađe 'cepKsession'! Uradi Maven Clean Install.");
        }

        // Ubacujemo konfiguraciju u Drools pri pokretanju
        wafConfig = new WafConfiguration();
        kieSession.insert(wafConfig);

        System.out.println("CEP Drools sesija je uspešno inicijalizovana. Početni kontekst: " + wafConfig.getCurrentContext());
    }

    // ISPRAVLJENO: Koristimo RequestEvent umesto starih Stringova
    public void processRequest(RequestEvent request) {
        // Pravimo ThreatScore za IP adresu ako već ne postoji
        boolean hasScore = false;
        for (Object fact : kieSession.getObjects()) {
            if (fact instanceof ThreatScore) {
                ThreatScore ts = (ThreatScore) fact;
                if (ts.getIpAddress().equals(request.getIpAddress())) {
                    hasScore = true;
                    break;
                }
            }
        }
        if (!hasScore) {
            kieSession.insert(new ThreatScore(request.getIpAddress()));
        }

        // Ubacujemo zahtev u Drools
        kieSession.insert(request);
        int firedRules = kieSession.fireAllRules();
        System.out.println("Obrađen zahtev sa IP: " + request.getIpAddress() + " | Okinuto pravila: " + firedRules);
    }

    public List<IpBan> getBannedIps() {
        List<IpBan> banovaneAdrese = new ArrayList<>();
        QueryResults rezultati = kieSession.getQueryResults("pribaviSveBanovaneIpAdrese");

        for (QueryResultsRow red : rezultati) {
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

    // Metoda za dinamičko dodavanje pravila preko šablona
    public void addDynamicRuleFromTemplate(String keyword, String threatName, String action) {
        System.out.println("--- GENERISANJE NOVOG PRAVILA IZ SABLONA ---");

        // 1. Priprema parametara
        Collection<Map<String, Object>> paramList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("threatName", threatName);
        params.put("action", action);
        paramList.add(params);

        // 2. Citanje sablona
        InputStream templateStream = this.getClass().getResourceAsStream("/rules/malicious-keyword.drt");
        if (templateStream == null) {
            throw new RuntimeException("Nije pronadjen .drt sablon!");
        }

        // 3. Kompajliranje sablona
        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String generatedDrl = compiler.compile(paramList, templateStream);
        System.out.println("Generisan DRL kod:\n" + generatedDrl);

        // 4. Ubacivanje u sesiju (Drools 7)
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(generatedDrl, ResourceType.DRL);

        InternalKnowledgeBase internalKieBase = (InternalKnowledgeBase) kieSession.getKieBase();
        internalKieBase.addPackages(kieHelper.build().getKiePackages());

        System.out.println("Novo pravilo je uspesno dodato u aktivnu sesiju!");
    }
}