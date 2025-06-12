package cz.ivosahlik.ordering.system.restaurant.service.domain.exception;

import cz.ivosahlik.ordering.system.domain.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
