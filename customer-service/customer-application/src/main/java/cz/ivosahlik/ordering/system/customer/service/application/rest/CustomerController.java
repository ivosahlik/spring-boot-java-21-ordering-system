package cz.ivosahlik.ordering.system.customer.service.application.rest;

import cz.ivosahlik.ordering.system.customer.service.domain.create.CreateCustomerCommand;
import cz.ivosahlik.ordering.system.customer.service.domain.create.CreateCustomerResponse;
import cz.ivosahlik.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/customers", produces = "application/vnd.api.v1+json")
public class CustomerController {

    private final CustomerApplicationService customerApplicationService;

    @PostMapping
    public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerCommand createCustomerCommand) {
        log.info("Creating customer with username: {}", createCustomerCommand.getUsername());
        CreateCustomerResponse response = customerApplicationService.createCustomer(createCustomerCommand);
        return ResponseEntity.ok(response);
    }

}
