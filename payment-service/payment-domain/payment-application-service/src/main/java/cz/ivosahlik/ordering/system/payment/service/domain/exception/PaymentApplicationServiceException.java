package cz.ivosahlik.ordering.system.payment.service.domain.exception;

import cz.ivosahlik.ordering.system.domain.exception.DomainException;

public class PaymentApplicationServiceException extends DomainException {

    public PaymentApplicationServiceException(String message) {
        super(message);
    }

    public PaymentApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
