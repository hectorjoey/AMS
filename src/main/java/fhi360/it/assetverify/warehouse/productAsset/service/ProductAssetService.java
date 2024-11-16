package fhi360.it.assetverify.warehouse.productAsset.service;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.warehouse.productAsset.dto.ProductAssetDto;
import fhi360.it.assetverify.warehouse.productAsset.model.ProductAsset;
import fhi360.it.assetverify.warehouse.productAsset.repository.ProductAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductAssetService {
    private final ProductAssetRepository productAssetRepository;

    public List<ProductAsset> getProductAssetByCountry(String country, Pageable pageable) {
        return productAssetRepository.findByCountry(country, pageable);
    }

    public List<ProductAsset> getProductAssetByState(String states, Pageable pageable) {
        return productAssetRepository.findByStates(states, pageable);
    }

    public ProductAsset createProductAsset(ProductAssetDto productAssetDto) throws ResourceNotFoundException {
        ProductAsset asset = productAssetRepository.findBySkuNumber(productAssetDto.getSkuNumber());
        if (asset != null) {
            throw new ResourceNotFoundException("Product with Sku Number %s already exists: " + productAssetDto.getSkuNumber());
        } else {
            ProductAsset productAsset = new ProductAsset();
            productAsset.setDateReceived(String.valueOf(LocalDate.now()));
            productAsset.setDescription(productAssetDto.getDescription());
            productAsset.setManufacturer(productAssetDto.getManufacturer());
            productAsset.setSkuNumber(productAssetDto.getSkuNumber());
            productAsset.setPoNumber(productAssetDto.getPoNumber());
            productAsset.setQuantityReceived(productAssetDto.getQuantityReceived());
            productAsset.setPurchasePrice(productAssetDto.getPurchasePrice());
            productAsset.setFunder(productAssetDto.getFunder());
            productAsset.setCountry(productAssetDto.getCountry());
            productAsset.setStates(productAssetDto.getStates());
            productAsset.setLastUpdatedDate(String.valueOf(LocalDate.now()));
            productAsset.setLastModifiedBy(productAssetDto.getLastModifiedBy());
            return productAssetRepository.save(productAsset);
        }
    }
}
