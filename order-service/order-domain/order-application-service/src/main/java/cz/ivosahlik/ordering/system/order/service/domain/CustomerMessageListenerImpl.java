package cz.ivosahlik.ordering.system.order.service.domain;

import cz.ivosahlik.ordering.system.order.service.domain.dto.message.CustomerModel;
import cz.ivosahlik.ordering.system.order.service.domain.entity.Customer;
import cz.ivosahlik.ordering.system.order.service.domain.exception.OrderDomainException;
import cz.ivosahlik.ordering.system.order.service.domain.mapper.OrderDataMapper;
import cz.ivosahlik.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import cz.ivosahlik.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;

    @Override
    public void customerCreated(CustomerModel customerModel) {
        Customer customer = customerRepository.save(orderDataMapper.customerModelToCustomer(customerModel));
        if (customer == null) {
            log.error("Customer could not be created in order database with id: {}", customerModel.getId());
            throw new OrderDomainException("Customer could not be created in order database with id " +
                    customerModel.getId());
        }
        log.info("Customer is created in order database with id: {}", customer.getId());
    }
}
