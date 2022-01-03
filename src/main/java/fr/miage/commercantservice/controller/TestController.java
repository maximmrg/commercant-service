package fr.miage.commercantservice.controller;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class TestController {
    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public TestController(WebClient.Builder loadBalancedWebClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
        this.lbFunction = lbFunction;
    }

    @RequestMapping("/hi")
    public Mono<Object> hi(@RequestParam(value = "name", defaultValue = "Mary") String name) {
        return loadBalancedWebClientBuilder.build().get().uri("http://localhost:8082/users")
                .retrieve().bodyToMono(Object.class);
    }

    @RequestMapping("/hello")
    public Mono<Object> hello(@RequestParam(value = "name", defaultValue = "John") String name) {
        return WebClient.builder()
                .filter(lbFunction)
                .build().get().uri("http://localhost:8082/users/1")
                .retrieve().bodyToMono(Object.class);
    }
}
