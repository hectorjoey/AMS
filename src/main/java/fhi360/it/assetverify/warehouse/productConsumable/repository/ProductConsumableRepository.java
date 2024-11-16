package fhi360.it.assetverify.warehouse.productConsumable.repository;

import fhi360.it.assetverify.warehouse.productConsumable.model.ProductConsumable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductConsumableRepository extends JpaRepository<ProductConsumable, Long> {
    List<ProductConsumable> findByCountryAndStates(String Country, String states, Pageable pageable);

    List<ProductConsumable> findByStatesAndCategoryAndCountry( String states,String category, String country, Pageable pageable);

    ProductConsumable findBySkuNumber(String skuNumber);


}
