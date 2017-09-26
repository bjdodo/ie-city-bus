package bjdodo.ie_city_bus.repository.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bjdodo.ie_city_bus.model.crud.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

	// @Query("select count(r) FROM Route r WHERE r.duid = ?1")
	// long countByDuid(String duid);

	public List<Route> findByShortName(String shortName);
}
