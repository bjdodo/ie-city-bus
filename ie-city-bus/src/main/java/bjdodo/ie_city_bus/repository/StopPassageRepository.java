package bjdodo.ie_city_bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bjdodo.ie_city_bus.model.StopPassage;

public interface StopPassageRepository extends JpaRepository<StopPassage, Long> {

	@Query("select count(bsp) FROM StopPassage bsp WHERE bsp.duid = ?1")
	long countByDuid(String duid);
}
