package bjdodo.ie_city_bus.repository;

import bjdodo.ie_city_bus.model.Vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}