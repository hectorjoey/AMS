package fhi360.it.assetverify.asset.repository;

import fhi360.it.assetverify.asset.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Asset findByAssetId(String assetId);
    Asset findBySerialNumber(String serialNumber);
    Page<Asset> findByOrderByIdAsc(Pageable pageable);
    Page<Asset> findByStates(String state, Pageable pageable);
    List<Asset> findByStates(String state);

    @Query("SELECT a FROM Asset a WHERE " +
            "a.serialNumber LIKE CONCAT('%', :query, '%') " +
            "OR a.emailAddress LIKE CONCAT('%', :query, '%') " +
            "OR a.manufacturer LIKE CONCAT('%', :query, '%') " +
            "OR a.phone LIKE CONCAT('%', :query, '%') " +
            "OR a.assignee LIKE CONCAT('%', :query, '%') " +
            "OR a.assetId LIKE CONCAT('%', :query, '%')")
    Page<Asset> searchAsset(String query, Pageable pageable);

    Page<Asset> findByCountry(String country, Pageable pageable);
    List<Asset> findByCountry(String country);

}
