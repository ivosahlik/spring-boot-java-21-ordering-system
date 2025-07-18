package cz.ivosahlik.ordering.system.order.service.domain.event;

import cz.ivosahlik.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {
    public OrderPaidEvent(Order order,
                          ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
