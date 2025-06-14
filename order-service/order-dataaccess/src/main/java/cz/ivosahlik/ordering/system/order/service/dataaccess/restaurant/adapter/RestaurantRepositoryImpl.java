package cz.ivosahlik.ordering.system.order.service.dataaccess.restaurant.adapter;

import cz.ivosahlik.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import cz.ivosahlik.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import cz.ivosahlik.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import cz.ivosahlik.ordering.system.order.service.domain.entity.Restaurant;
import cz.ivosahlik.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
