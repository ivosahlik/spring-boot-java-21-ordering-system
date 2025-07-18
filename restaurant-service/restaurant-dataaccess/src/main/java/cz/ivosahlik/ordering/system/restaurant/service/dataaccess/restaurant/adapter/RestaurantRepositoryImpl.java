package cz.ivosahlik.ordering.system.restaurant.service.dataaccess.restaurant.adapter;

import cz.ivosahlik.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import cz.ivosahlik.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import cz.ivosahlik.ordering.system.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import cz.ivosahlik.ordering.system.restaurant.service.domain.entity.Restaurant;
import cz.ivosahlik.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository,
                                    RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
