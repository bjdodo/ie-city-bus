package bjdodo.ie_city_bus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import bjdodo.ie_city_bus.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {

	// we'll need to be able to list unfinished trips here
	@Query("select t FROM Trip t WHERE t.finished = 0")
	List<Trip> getUnfinishedTrips();

	@Transactional
	@Modifying
	@Query("update Trip t set t.finished=1 where t.duid not in (select v.tripDuid from Vehicle v)")
	int closeFinishedTrips();

}
