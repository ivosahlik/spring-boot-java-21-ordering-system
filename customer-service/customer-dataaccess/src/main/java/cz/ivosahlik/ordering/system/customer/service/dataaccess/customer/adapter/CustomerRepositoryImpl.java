package cz.ivosahlik.ordering.system.customer.service.dataaccess.customer.adapter;

import cz.ivosahlik.ordering.system.customer.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import cz.ivosahlik.ordering.system.customer.service.dataaccess.customer.repository.CustomerJpaRepository;
import cz.ivosahlik.ordering.system.customer.service.domain.entity.Customer;
import cz.ivosahlik.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(
                customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }
}
