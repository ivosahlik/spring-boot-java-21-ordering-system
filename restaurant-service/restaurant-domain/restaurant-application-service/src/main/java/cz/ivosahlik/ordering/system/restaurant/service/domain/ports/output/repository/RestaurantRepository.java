package cz.ivosahlik.ordering.system.restaurant.service.domain.ports.output.repository;

import cz.ivosahlik.ordering.system.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
