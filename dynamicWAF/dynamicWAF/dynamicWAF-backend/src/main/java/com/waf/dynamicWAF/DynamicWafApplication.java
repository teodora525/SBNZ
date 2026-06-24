package com.waf.dynamicWAF;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DynamicWafApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicWafApplication.class, args);
	}
}