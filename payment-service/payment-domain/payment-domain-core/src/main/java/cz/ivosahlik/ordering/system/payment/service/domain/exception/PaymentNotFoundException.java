package cz.ivosahlik.ordering.system.payment.service.domain.exception;

import cz.ivosahlik.ordering.system.domain.exception.DomainException;

public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
