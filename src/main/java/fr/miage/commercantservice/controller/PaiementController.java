package fr.miage.commercantservice.controller;

import fr.miage.commercantservice.input.PaiementInput;
import fr.miage.commercantservice.validator.PaiementValidator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/paiements", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PaiementController {

    RestTemplate template;
    LoadBalancerClientFactory clientFactory;

    @PostMapping
    @CircuitBreaker(name = "commercant-service", fallbackMethod = "fallbackPaiementCall")
    public PaiementInput createPaiement(@RequestBody @Valid PaiementInput paiementInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RoundRobinLoadBalancer loadBalancer = clientFactory.getInstance("gateway-service", RoundRobinLoadBalancer.class);
        ServiceInstance instance = loadBalancer.choose().block().getServer();
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/bank-service/paiements";

        HttpEntity<PaiementInput> httpEntity = new HttpEntity<>(paiementInput, headers);
        PaiementInput response = template.postForObject(url, httpEntity, PaiementInput.class);

        return response;
    }

    private PaiementInput fallBackPaiementCall(RuntimeException e) {
        return new PaiementInput("Echec");
    }
}
