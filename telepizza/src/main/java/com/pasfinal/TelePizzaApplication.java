package com.pasfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.pasfinal")
@EnableFeignClients
public class TelePizzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelePizzaApplication.class, args);
	}

}
