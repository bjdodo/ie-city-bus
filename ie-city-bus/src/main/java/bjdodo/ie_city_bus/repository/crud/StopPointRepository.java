package bjdodo.ie_city_bus.repository.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import bjdodo.ie_city_bus.model.crud.StopPoint;

public interface StopPointRepository extends JpaRepository<StopPoint, Long> {

	// @Query("select count(bsp) FROM StopPoint bsp WHERE bsp.duid = ?1")
	// long countByDuid(String duid);
}
