package cz.ivosahlik.ordering.system.saga.order;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class SagaConstants {

    public static final String ORDER_SAGA_NAME = "OrderProcessingSaga";
}
