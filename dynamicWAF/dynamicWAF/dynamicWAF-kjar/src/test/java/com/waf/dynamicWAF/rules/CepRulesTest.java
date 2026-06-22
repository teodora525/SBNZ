package com.waf.dynamicWAF.rules;

import com.waf.dynamicWAF.model.RequestEvent;
import com.waf.dynamicWAF.model.ThreatAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.time.SessionPseudoClock;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CepRulesTest {

    private KieSession kieSession;
    private SessionPseudoClock clock;

    @BeforeEach
    public void setup() {
        // Koristimo KieHelper da eksplicitno i direktno učitamo tvoja pravila
        org.kie.internal.utils.KieHelper kieHelper = new org.kie.internal.utils.KieHelper();
        kieHelper.addResource(org.kie.internal.io.ResourceFactory.newClassPathResource("rules/cep-rules.drl"), org.kie.api.io.ResourceType.DRL);
        kieHelper.addResource(org.kie.internal.io.ResourceFactory.newClassPathResource("rules/waf-rules.drl"), org.kie.api.io.ResourceType.DRL);

        // Ugrađeni detektiv koji nam odmah puca ako postoji greška u kucanju unutar .drl fajla
        if (kieHelper.verify().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            System.out.println("=== GREŠKE U PRAVILIMA ===");
            System.out.println(kieHelper.verify().getMessages());
            throw new RuntimeException("Pravila se nisu uspešno iskompajlirala!");
        }

        // Forsiramo STREAM mod koji je neophodan za CEP
        org.kie.api.KieBaseConfiguration kBaseConfig = KieServices.Factory.get().newKieBaseConfiguration();
        kBaseConfig.setOption(org.kie.api.conf.EventProcessingOption.STREAM);
        org.kie.api.KieBase kieBase = kieHelper.build(kBaseConfig);

        // Konfigurišemo i ubacujemo Pseudo sat
        KieSessionConfiguration config = KieServices.Factory.get().newKieSessionConfiguration();
        config.setOption(ClockTypeOption.get("pseudo"));

        kieSession = kieBase.newKieSession(config, null);
        clock = kieSession.getSessionClock();
    }

    @Test
    public void testBruteForceDetection() {
        String attackerIp = "192.168.1.50";

        // 1. Simuliramo 5 brzih ThreatAlert događaja sa iste IP adrese (umesto RequestEvent-a)
        for (int i = 0; i < 5; i++) {

            ThreatAlert alert = new ThreatAlert(attackerIp, "Test pretnja");            // Prilagodi naziv setera ako ti se atribut u Javi zove drugačije (npr. setIpAddress)
            kieSession.insert(alert);

            // Unapređujemo vreme za po 5 sekundi između svakog napada.
            // Ukupno će proći 25 sekundi, što savršeno upada u tvoj prozor od 1 minuta (1m).
            clock.advanceTime(5, TimeUnit.SECONDS);
        }

        // 2. Okidamo sva pravila
        int firedRules = kieSession.fireAllRules();

        // 3. Proveravamo da li je sistem prepoznao napad
        System.out.println("Broj okinutih pravila: " + firedRules);

        // Asertacija 1: Pravilo se okinulo tačno 1 put
        assertEquals(1, firedRules, "Brute Force pravilo se nije okinulo kada je trebalo!");

        // 4. EKSTRA ZA ODBRANU: Proveravamo da li je Drools ubacio IpBan u memoriju
        // (Ovo asistenti obožavaju da vide, jer dokazuje da je onaj 'insert(new IpBan...)' iz pravila odradio posao)
        long banCount = kieSession.getObjects().stream()
                .filter(obj -> obj instanceof com.waf.dynamicWAF.model.IpBan)
                .count();
        assertEquals(1, banCount, "IpBan objekat nije uspešno ubačen u memoriju!");
    }
}