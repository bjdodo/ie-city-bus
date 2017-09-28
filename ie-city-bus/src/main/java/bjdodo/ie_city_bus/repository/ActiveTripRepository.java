package bjdodo.ie_city_bus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bjdodo.ie_city_bus.model.ActiveTrip;
import bjdodo.ie_city_bus.model.TripDetail;


@Repository
public interface ActiveTripRepository extends JpaRepository<ActiveTrip, Long> {

	@Query("select atp FROM ActiveTrip atp order by atp.routeShortName")
	List<ActiveTrip> getActiveTrips();

	@Query("select atp FROM ActiveTrip atp where atp.routeShortName=?1")
	List<ActiveTrip> getRouteActiveTrips(String routeShortName);

	@Query("select atp FROM ActiveTrip atp where atp.tripId in (?1)")
	List<ActiveTrip> getActiveTripsById(long[] tripIds);

	@Query("select td\r\n" +
			"from TripDetail td\r\n" +
			"where td.tripId=?1\r\n" +
			"order by td.scheduledArrival")
	List<TripDetail> getTripDetails(Long tripId);
}
