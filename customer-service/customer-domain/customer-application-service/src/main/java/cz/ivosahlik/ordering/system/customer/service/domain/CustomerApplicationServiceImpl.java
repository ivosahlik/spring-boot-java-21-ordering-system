package cz.ivosahlik.ordering.system.customer.service.domain;


import cz.ivosahlik.ordering.system.customer.service.domain.create.CreateCustomerCommand;
import cz.ivosahlik.ordering.system.customer.service.domain.create.CreateCustomerResponse;
import cz.ivosahlik.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import cz.ivosahlik.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import cz.ivosahlik.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService;
import cz.ivosahlik.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final CustomerCreateCommandHandler customerCreateCommandHandler;
    private final CustomerDataMapper customerDataMapper;
    private final CustomerMessagePublisher customerMessagePublisher;

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent = customerCreateCommandHandler.createCustomer(createCustomerCommand);
        customerMessagePublisher.publish(customerCreatedEvent);
        return customerDataMapper
                .customerToCreateCustomerResponse(customerCreatedEvent.getCustomer(),
                        "Customer saved successfully!");
    }
}
