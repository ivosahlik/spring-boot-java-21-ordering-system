package cz.ivosahlik.ordering.system.payment.service.domain;

import cz.ivosahlik.ordering.system.domain.valueobject.PaymentStatus;
import cz.ivosahlik.ordering.system.outbox.OutboxStatus;
import cz.ivosahlik.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import cz.ivosahlik.ordering.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import cz.ivosahlik.ordering.system.payment.service.domain.dto.PaymentRequest;
import cz.ivosahlik.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import cz.ivosahlik.ordering.system.domain.valueobject.PaymentOrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cz.ivosahlik.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = PaymentServiceApplication.class)
class PaymentRequestMessageListenerTest {

    @Autowired
    private PaymentRequestMessageListener paymentRequestMessageListener;

    @Autowired
    private OrderOutboxJpaRepository orderOutboxJpaRepository;

    private static final String CUSTOMER_ID = "d215b5f8-0249-4dc5-89a3-51fd148cfb41";
    private static final BigDecimal PRICE = new BigDecimal("100");

    @Test
    void testDoublePayment() {
        String sagaId = UUID.randomUUID().toString();
        paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        try {
            paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred with sql state: {}",
                    ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
        }
        assertOrderOutbox(sagaId);
    }

    @Test
    void testDoublePaymentWithThreads() {
        String sagaId = UUID.randomUUID().toString();
        ExecutorService executor = null;

        try {
            executor = Executors.newFixedThreadPool(2);
            List<Callable<Object>> tasks = new ArrayList<>();

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 1 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 2 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            executor.invokeAll(tasks);

            assertOrderOutbox(sagaId);
        } catch (InterruptedException e) {
            log.error("Error calling complete payment!", e);
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    private void assertOrderOutbox(String sagaId) {
        Optional<OrderOutboxEntity> orderOutboxEntity = orderOutboxJpaRepository
                .findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(ORDER_SAGA_NAME,
                        UUID.fromString(sagaId),
                        PaymentStatus.COMPLETED,
                        OutboxStatus.STARTED);
        assertTrue(orderOutboxEntity.isPresent());
        assertEquals(orderOutboxEntity.get().getSagaId().toString(), sagaId);
    }

    private PaymentRequest getPaymentRequest(String sagaId) {
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .orderId(UUID.randomUUID().toString())
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .customerId(CUSTOMER_ID)
                .price(PRICE)
                .createdAt(Instant.now())
                .build();
    }

}
