package cz.ivosahlik.ordering.system.order.service.domain.outbox.model.approval;

import cz.ivosahlik.ordering.system.domain.valueobject.OrderStatus;
import cz.ivosahlik.ordering.system.outbox.OutboxStatus;
import cz.ivosahlik.ordering.system.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    @Setter
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    @Setter
    private SagaStatus sagaStatus;
    @Setter
    private OrderStatus orderStatus;
    @Setter
    private OutboxStatus outboxStatus;
    private int version;

}
