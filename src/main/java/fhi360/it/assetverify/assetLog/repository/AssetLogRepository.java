package fhi360.it.assetverify.assetLog.repository;

import fhi360.it.assetverify.assetLog.model.AssetLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetLogRepository extends JpaRepository<AssetLog, Long> {


    List<AssetLog> findByDateBetween(String startDate, String endDate);

    List<AssetLog> findByDateBetween(String startDate, String endDate, Pageable pageable);

    List<AssetLog> findByStates(String state, Pageable pageable);

    List<AssetLog> findByCountry(String country, Pageable pageable);

}
