package com.newtouch.uctp.module.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UctpBusinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(UctpBusinessApplication.class, args);
	}

}
