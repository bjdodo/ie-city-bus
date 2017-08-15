package bjdodo.ie_city_bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bjdodo.ie_city_bus.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {

}
