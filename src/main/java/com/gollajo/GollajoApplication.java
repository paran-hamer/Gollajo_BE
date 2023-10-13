package com.gollajo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class GollajoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GollajoApplication.class, args);
	}

}
