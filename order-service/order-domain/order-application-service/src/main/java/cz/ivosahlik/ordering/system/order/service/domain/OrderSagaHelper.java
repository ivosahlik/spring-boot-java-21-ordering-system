package cz.ivosahlik.ordering.system.order.service.domain;

import cz.ivosahlik.ordering.system.domain.valueobject.OrderId;
import cz.ivosahlik.ordering.system.domain.valueobject.OrderStatus;
import cz.ivosahlik.ordering.system.order.service.domain.entity.Order;
import cz.ivosahlik.ordering.system.order.service.domain.exception.OrderNotFoundException;
import cz.ivosahlik.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import cz.ivosahlik.ordering.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if (orderResponse.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found!");
        }
        return orderResponse.get();
    }

    void saveOrder(Order order) {
        orderRepository.save(order);
    }

    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PAID -> SagaStatus.PROCESSING;
            case APPROVED -> SagaStatus.SUCCEEDED;
            case CANCELLING -> SagaStatus.COMPENSATING;
            case CANCELLED -> SagaStatus.COMPENSATED;
            default -> SagaStatus.STARTED;
        };
    }
}
