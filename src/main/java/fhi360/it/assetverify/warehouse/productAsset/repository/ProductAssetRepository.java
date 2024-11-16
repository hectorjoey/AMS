package fhi360.it.assetverify.warehouse.productAsset.repository;

import fhi360.it.assetverify.warehouse.productAsset.model.ProductAsset;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, Long> {
    List<ProductAsset> findByCountry(String country, Pageable pageable);
    List<ProductAsset> findByStates(String states, Pageable pageable);
    ProductAsset findBySkuNumber(String skuNumber);
}
