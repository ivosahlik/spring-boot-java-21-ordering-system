package cz.ivosahlik.ordering.system.order.service.domain.event;

import cz.ivosahlik.ordering.system.domain.event.DomainEvent;
import cz.ivosahlik.ordering.system.order.service.domain.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@RequiredArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {
    private final Order order;
    private final ZonedDateTime createdAt;
}
