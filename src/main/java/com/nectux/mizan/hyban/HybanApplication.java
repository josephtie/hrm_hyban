package com.nectux.mizan.hyban;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling

public class HybanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HybanApplication.class, args);
	}

}
