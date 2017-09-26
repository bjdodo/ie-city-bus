package bjdodo.ie_city_bus.repository.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import bjdodo.ie_city_bus.model.crud.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {

	// we'll need to be able to list unfinished trips here
	@Query("select t FROM Trip t WHERE t.finished = 0")
	List<Trip> getUnfinishedTrips();

	@Transactional
	@Modifying
	@Query("update Trip t set t.finished=1 where t.finished=0 and t.id not in (select v.currentTripId from Vehicle v where v.isDeleted=false)")
	// @Query("update Trip t set t.finished=1 where t.finished=0 and ((t.id not in
	// (select v.currentTripId from Vehicle v)) or (t.actualFinish<now()))")
	int closeFinishedTrips();

	public Trip findByDuid(String duid);
}
