package com.igorzaitcev.inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.igorzaitcev.inventory.utils.DBRegistrator;

@SpringBootApplication
public class ErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpApplication.class, args);
	}

	@Profile("!test")
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> DBRegistrator.populateDB();

	}

}
