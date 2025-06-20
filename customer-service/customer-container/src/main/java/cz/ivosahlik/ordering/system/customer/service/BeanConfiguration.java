package cz.ivosahlik.ordering.system.customer.service;

import cz.ivosahlik.ordering.system.customer.service.domain.CustomerDomainService;
import cz.ivosahlik.ordering.system.customer.service.domain.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
