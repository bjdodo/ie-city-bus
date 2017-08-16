package bjdodo.ie_city_bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bjdodo.ie_city_bus.model.BusStopPoint;

public interface BusStopPointRepository extends JpaRepository<BusStopPoint, Long> {

	@Query("select count(bsp) FROM BusStopPoint bsp WHERE bsp.duid = ?1")
	int countByDuid(String duid);
}
