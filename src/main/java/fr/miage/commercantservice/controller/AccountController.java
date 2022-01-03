package fr.miage.commercantservice.controller;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/users/{userId}/accounts")
public class AccountController {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public AccountController(WebClient.Builder loadBalancedWebClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
        this.lbFunction = lbFunction;
    }

    @GetMapping
    public Mono<Object> hi (@PathVariable(value = "userId") String userId){
       return loadBalancedWebClientBuilder.build().get().uri("http://localhost:8082/users/" + userId + "/accounts")
               .retrieve().bodyToMono(Object.class);
   }

   @GetMapping(value = "/{accountId}/solde")
    public Mono<Object> getAccountSolde(@PathVariable(value = "userId") String userId, @PathVariable(value = "accountId") String accountId){
        return WebClient.builder()
                .filter(lbFunction)
                .build().get().uri("http://localhost:8082/users/" + userId + "/accounts")
                .retrieve().bodyToMono(Object.class);
   }
}
