package cz.ivosahlik.ordering.system.restaurant.service.domain.ports.input.message.listener;

import cz.ivosahlik.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {
    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
