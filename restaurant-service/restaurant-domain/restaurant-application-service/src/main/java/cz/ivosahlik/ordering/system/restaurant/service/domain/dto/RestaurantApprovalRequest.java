package cz.ivosahlik.ordering.system.restaurant.service.domain.dto;

import cz.ivosahlik.ordering.system.domain.valueobject.RestaurantOrderStatus;
import cz.ivosahlik.ordering.system.restaurant.service.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalRequest {
    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantOrderStatus restaurantOrderStatus;
    private java.util.List<Product> products;
    private java.math.BigDecimal price;
    private java.time.Instant createdAt;
}
