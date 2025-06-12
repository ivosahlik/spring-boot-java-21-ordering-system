package cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundException;
import cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.ApprovalOutboxDataAccessMapper;
import cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import cz.ivosahlik.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import cz.ivosahlik.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import cz.ivosahlik.ordering.system.outbox.OutboxStatus;
import cz.ivosahlik.ordering.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper;

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        return approvalOutboxDataAccessMapper
                .approvalOutboxEntityToOrderApprovalOutboxMessage(approvalOutboxJpaRepository
                        .save(approvalOutboxDataAccessMapper
                                .orderCreatedOutboxMessageToOutboxEntity(orderApprovalOutboxMessage)));
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String sagaType,
                                                                                       OutboxStatus outboxStatus,
                                                                       SagaStatus... sagaStatus) {
        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(sagaType, outboxStatus,
                List.of(sagaStatus))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox object " +
                        "could be found for saga type " + sagaType))
                .stream()
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage)
                .toList());
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
                                                                                 UUID sagaId,
                                                                                 SagaStatus... sagaStatus) {
        return approvalOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, List.of(sagaStatus))
                .map(approvalOutboxDataAccessMapper::approvalOutboxEntityToOrderApprovalOutboxMessage);

    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus,
                List.of(sagaStatus));
    }
}
