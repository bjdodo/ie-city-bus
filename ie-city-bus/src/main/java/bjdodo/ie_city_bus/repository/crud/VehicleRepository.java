package bjdodo.ie_city_bus.repository.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import bjdodo.ie_city_bus.model.crud.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	// In theory this should always return only one vehicle (or null)
	// In practice I have seen 2 buses assigned to the same trip, one
	// of those was sitting in the garage
	List<Vehicle> findByCurrentTripId(long tripId);

	public Vehicle findByDuid(String duid);

	@Transactional
	@Modifying
	@Query("update Vehicle v set v.currentTripId=?2 where v.id=?1")
	int updateTripId(long vehicleId, long tripId);

}