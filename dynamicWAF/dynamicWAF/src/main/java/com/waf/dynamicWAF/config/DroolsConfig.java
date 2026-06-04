package com.waf.dynamicWAF.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static final String RULES_PATH = "rules/cep-rules.drl";

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // 1. Učitavamo .drl fajl
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH));

        // 2. KLJUČNO: Dinamički kreiramo kmodule.xml koji globalno forsira STREAM mod
        String kmoduleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kmodule xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"CEPBase\" default=\"true\" eventProcessingMode=\"stream\" equalsBehavior=\"identity\">\n" +
                "    <ksession name=\"CEPSession\" default=\"true\" clockType=\"realtime\"/>\n" +
                "  </kbase>\n" +
                "</kmodule>";
        kieFileSystem.write("src/main/resources/META-INF/kmodule.xml", kmoduleXml);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}