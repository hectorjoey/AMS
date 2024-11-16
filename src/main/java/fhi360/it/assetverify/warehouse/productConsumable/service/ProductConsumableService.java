package fhi360.it.assetverify.warehouse.productConsumable.service;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.warehouse.consumableLog.model.ConsumableLog;
import fhi360.it.assetverify.warehouse.consumableLog.repository.ConsumableLogRepository;
import fhi360.it.assetverify.warehouse.productConsumable.dto.ProductConsumableDto;
import fhi360.it.assetverify.warehouse.productConsumable.model.ProductConsumable;
import fhi360.it.assetverify.warehouse.productConsumable.repository.ProductConsumableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ProductConsumableService {
    private final ProductConsumableRepository productConsumableRepository;
    private final  ConsumableLogRepository consumableLogRepository;



    public ProductConsumable createProductConsumable(ProductConsumableDto productConsumableDto) throws ResourceNotFoundException{
        ProductConsumable consumable = productConsumableRepository.findBySkuNumber(productConsumableDto.getSkuNumber());
        if (consumable == null){
            throw new ResourceNotFoundException("Product with %s exist " + productConsumableDto.getSkuNumber());
        }
        else {
            ProductConsumable productConsumable = new ProductConsumable();
            productConsumable.setHasExpired(false);
            productConsumable.setDateReceived(String.valueOf(LocalDate.now()));
            productConsumable.setDate(String.valueOf(LocalDate.now()));
            productConsumable.setLastModifiedBy(productConsumable.getLastModifiedBy());
            productConsumable.setLastModifiedDate(String.valueOf(LocalDate.now()));


//            ConsumableLog log = consumableLogRepository.findByProductConsumableId(productConsumable.getId());
//            ConsumableLog consumableLog = new ConsumableLog();
//            consumableLog.setDate(log.getDate());
//            consumableLog.setHasExpired(log.isHasExpired());
//            consumableLog.setLastModifiedBy(log.getLastModifiedBy());
//            consumableLog.setLastModifiedDate(log.getLastModifiedDate());

            return productConsumableRepository.save(productConsumable);

        }
    }


}
