package cz.ivosahlik.ordering.system.customer.service.domain;

import cz.ivosahlik.ordering.system.customer.service.domain.create.CreateCustomerCommand;
import cz.ivosahlik.ordering.system.customer.service.domain.entity.Customer;
import cz.ivosahlik.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import cz.ivosahlik.ordering.system.customer.service.domain.exception.CustomerDomainException;
import cz.ivosahlik.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import cz.ivosahlik.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
class CustomerCreateCommandHandler {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;

    @Transactional
    public CustomerCreatedEvent createCustomer(CreateCustomerCommand createCustomerCommand) {
        Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
        CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(customer);
        Customer savedCustomer = customerRepository.createCustomer(customer);
        if (savedCustomer == null) {
            log.error("Could not save customer with id: {}", createCustomerCommand.getCustomerId());
            throw new CustomerDomainException("Could not save customer with id " +
                    createCustomerCommand.getCustomerId());
        }
        log.info("Returning CustomerCreatedEvent for customer id: {}", createCustomerCommand.getCustomerId());
        return customerCreatedEvent;
    }
}
