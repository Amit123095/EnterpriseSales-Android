package com.amit.enterprisesales.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		excludeName = {
				"org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration"
		}
)
public class EnterpriseSalesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseSalesBackendApplication.class, args);
	}
}