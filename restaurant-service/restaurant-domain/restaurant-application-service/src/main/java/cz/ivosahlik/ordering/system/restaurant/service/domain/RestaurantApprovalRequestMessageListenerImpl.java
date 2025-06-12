package cz.ivosahlik.ordering.system.restaurant.service.domain;

import cz.ivosahlik.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import cz.ivosahlik.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        restaurantApprovalRequestHelper.persistOrderApproval(restaurantApprovalRequest);
    }
}
