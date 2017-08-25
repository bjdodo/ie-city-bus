package bjdodo.ie_city_bus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import bjdodo.ie_city_bus.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	public Vehicle findByDuid(String duid);

	@Transactional
	@Modifying
	@Query("update Vehicle v set v.currentTripId=?2 where v.id=?1")
	int updateTripId(long vehicleId, long tripId);

	@Query("update Vehicle v set v.currentTripId=?2 where v.id=?1")
	List<Vehicle> findVehiclesForRoute(String routeShortName);
}