package bjdodo.ie_city_bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bjdodo.ie_city_bus.model.VehicleHistoryLog;

public interface VehicleHistoryLogRepository extends JpaRepository<VehicleHistoryLog, Long> {

}
