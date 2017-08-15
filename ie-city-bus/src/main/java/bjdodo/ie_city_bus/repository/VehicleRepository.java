package bjdodo.ie_city_bus.repository;

import bjdodo.ie_city_bus.model.Vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	
	@Query("select count(v) FROM Vehicle v WHERE v.duid = ?1")
	int countByDuid(String duid);
}