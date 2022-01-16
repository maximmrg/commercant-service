package fr.miage.commercantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class CommercantServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommercantServiceApplication.class, args);
	}

	@Bean
	RestTemplate template() {
		return new RestTemplate();
	}

}
