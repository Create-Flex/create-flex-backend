package com.mcn.in4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class In4Application {

	public static void main(String[] args) {
		SpringApplication.run(In4Application.class, args);
	}

}