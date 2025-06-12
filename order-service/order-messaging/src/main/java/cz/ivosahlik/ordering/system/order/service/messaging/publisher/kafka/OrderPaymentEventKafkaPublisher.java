package cz.ivosahlik.ordering.system.order.service.messaging.publisher.kafka;

import cz.ivosahlik.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import cz.ivosahlik.ordering.system.kafka.producer.KafkaMessageHelper;
import cz.ivosahlik.ordering.system.kafka.producer.service.KafkaProducer;
import cz.ivosahlik.ordering.system.order.service.domain.config.OrderServiceConfigData;
import cz.ivosahlik.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import cz.ivosahlik.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import cz.ivosahlik.ordering.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import cz.ivosahlik.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import cz.ivosahlik.ordering.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {
        OrderPaymentEventPayload orderPaymentEventPayload =
                kafkaMessageHelper.getOrderEventPayload(orderPaymentOutboxMessage.getPayload(),
                        OrderPaymentEventPayload.class);

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

        log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
                orderPaymentEventPayload.getOrderId(),
                sagaId);

        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    sagaId,
                    paymentRequestAvroModel,
                    getKafkaCallback(
                            orderPaymentOutboxMessage,
                            outboxCallback,
                            paymentRequestAvroModel,
                            orderPaymentEventPayload
                    )
            );

            log.info("OrderPaymentEventPayload sent to Kafka for order id: {} and saga id: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
           log.error("Error while sending OrderPaymentEventPayload" +
                           " to kafka with order id: {} and saga id: {}, error: {}",
                   orderPaymentEventPayload.getOrderId(), sagaId, e.getMessage());
        }


    }

    private BiConsumer<SendResult<String, PaymentRequestAvroModel>, Throwable> getKafkaCallback(
            OrderPaymentOutboxMessage orderPaymentOutboxMessage,
            BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback,
            PaymentRequestAvroModel paymentRequestAvroModel,
            OrderPaymentEventPayload orderPaymentEventPayload) {
        return kafkaMessageHelper.getKafkaCallback(
                orderServiceConfigData.getPaymentRequestTopicName(),
                paymentRequestAvroModel,
                orderPaymentOutboxMessage,
                outboxCallback,
                orderPaymentEventPayload.getOrderId(),
                "PaymentRequestAvroModel"
        );
    }
}
