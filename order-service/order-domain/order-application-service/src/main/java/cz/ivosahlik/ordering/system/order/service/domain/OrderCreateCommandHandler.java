package cz.ivosahlik.ordering.system.order.service.domain;

import cz.ivosahlik.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import cz.ivosahlik.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import cz.ivosahlik.ordering.system.order.service.domain.event.OrderCreatedEvent;
import cz.ivosahlik.ordering.system.order.service.domain.mapper.OrderDataMapper;
import cz.ivosahlik.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import cz.ivosahlik.ordering.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        CreateOrderResponse createOrderResponse = orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(),
                "Order created successfully");

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(orderCreatedEvent),
                orderCreatedEvent.getOrder().getOrderStatus(),
                orderSagaHelper.orderStatusToSagaStatus(orderCreatedEvent.getOrder().getOrderStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID());

        log.info("Returning CreateOrderResponse with order id: {}", orderCreatedEvent.getOrder().getId());

        return createOrderResponse;
    }
}
