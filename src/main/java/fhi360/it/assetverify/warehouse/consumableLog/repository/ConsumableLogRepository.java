package fhi360.it.assetverify.warehouse.consumableLog.repository;

import fhi360.it.assetverify.warehouse.consumableLog.model.ConsumableLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumableLogRepository extends JpaRepository<ConsumableLog, Long> {

    Page<ConsumableLog> findByOrderByIdAsc(Pageable pageable);

    List<ConsumableLog> findByProductConsumableId(Long productConsumableId);

    List<ConsumableLog> findByStatesAndDateIssuedBetween(String states, String startDate, String endDate, Pageable pageable);

    List<ConsumableLog> findByDescription(String description);

    List<ConsumableLog> findByDateReceived(String date);

    List<ConsumableLog>findByStates(String states, Pageable pageable);

    List<ConsumableLog> findByCountry(String country, Pageable pageable);

//    ConsumableLog findByProductConsumableId(Long productConsumableId);
}
