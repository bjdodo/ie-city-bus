package bjdodo.ie_city_bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bjdodo.ie_city_bus.model.StopPoint;

public interface StopPointRepository extends JpaRepository<StopPoint, Long> {

	@Query("select count(bsp) FROM StopPoint bsp WHERE bsp.duid = ?1")
	long countByDuid(String duid);
}
