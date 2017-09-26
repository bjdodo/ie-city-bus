package bjdodo.ie_city_bus.repository.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bjdodo.ie_city_bus.model.crud.StopPassage;

public interface StopPassageRepository extends JpaRepository<StopPassage, Long> {

	// @Query("select count(bsp) FROM StopPassage bsp WHERE bsp.duid = ?1")
	// long countByDuid(String duid);

	public StopPassage findByDuid(String duid);

	@Query("select sp FROM StopPassage sp WHERE sp.tripId=?1")
	public List<StopPassage> findByTripId(long tripId);
}
