package com.pasfinal.estoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EstoqueMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstoqueMicroserviceApplication.class, args);
	}

}
