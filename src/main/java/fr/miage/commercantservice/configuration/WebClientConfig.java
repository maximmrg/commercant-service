package fr.miage.commercantservice.configuration;

import fr.miage.commercantservice.CommercantServiceApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@LoadBalancerClient(name = "commerce", configuration = CommercantServiceApplication.class)
public class WebClientConfig {

    @Bean
    public RoundRobinLoadBalancer roundRobinLoadBalancer(LoadBalancerClientFactory clientFactory, Environment environment) {
        String serviceId = clientFactory.getName(environment);
        return new RoundRobinLoadBalancer(clientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId, -1);
    }
}
