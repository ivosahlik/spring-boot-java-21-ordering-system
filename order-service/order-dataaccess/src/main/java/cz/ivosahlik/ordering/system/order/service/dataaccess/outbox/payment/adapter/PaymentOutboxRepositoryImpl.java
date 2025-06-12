package cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.payment.adapter;

import cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.payment.exception.PaymentOutboxNotFoundException;
import cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.payment.mapper.PaymentOutboxDataAccessMapper;
import cz.ivosahlik.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import cz.ivosahlik.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import cz.ivosahlik.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import cz.ivosahlik.ordering.system.outbox.OutboxStatus;
import cz.ivosahlik.ordering.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;
    private final PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return paymentOutboxDataAccessMapper.paymentOutboxEntityToOrderPaymentOutboxMessage(
                paymentOutboxJpaRepository.save(
                        paymentOutboxDataAccessMapper.orderPaymentOutboxMessageToOutboxEntity(orderPaymentOutboxMessage)
                )
        );
    }

    @Override
    public Optional<List<OrderPaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String sagaType,
                                                                                            OutboxStatus outboxStatus,
                                                                                            SagaStatus... sagaStatus) {
        return Optional.of(paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(sagaType,
                        outboxStatus,
                        List.of(sagaStatus))
                .orElseThrow(() -> new PaymentOutboxNotFoundException("Payment outbox object " +
                        "could not be found for saga type " + sagaType))
                .stream()
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage)
                .toList());
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
                                                                                UUID sagaId,
                                                                                SagaStatus... sagaStatus) {
        return paymentOutboxJpaRepository
                .findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatus))
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus, List.of(sagaStatus));
    }
}
