package fr.miage.commercantservice.controller;

import fr.miage.commercantservice.input.PaiementInput;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/paiements", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaiementController {

    RestTemplate template;
    LoadBalancerClientFactory clientFactory;

    public PaiementController(RestTemplate template, LoadBalancerClientFactory clientFactory) {
        this.template = template;
        this.clientFactory = clientFactory;
    }

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

    private PaiementInput fallbackPaiementCall(RuntimeException runEx) {
        runEx.printStackTrace();
        return new PaiementInput("Echec");
    }
}
