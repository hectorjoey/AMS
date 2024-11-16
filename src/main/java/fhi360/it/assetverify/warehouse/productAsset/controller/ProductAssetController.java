package fhi360.it.assetverify.warehouse.productAsset.controller;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.warehouse.productAsset.dto.ProductAssetDto;
import fhi360.it.assetverify.warehouse.productAsset.model.ProductAsset;
import fhi360.it.assetverify.warehouse.productAsset.service.ProductAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class ProductAssetController {

    private final ProductAssetService productAssetService;

    @GetMapping("product/asset/country")
    public ResponseEntity<Object> getProductAssetByCountry(String country, Pageable pageable){
        return new ResponseEntity<>(productAssetService.getProductAssetByCountry(country, pageable), HttpStatus.OK);
    }

    @GetMapping("product/asset/states")
    public ResponseEntity<Object> getProductAssetByStates(String states, Pageable pageable){
        return new ResponseEntity<>(productAssetService.getProductAssetByState(states, pageable), HttpStatus.OK);
    }

    @PostMapping("product/asset/")
    public ResponseEntity<ProductAsset> createProductAsset(@RequestBody ProductAssetDto productAsset) throws ResourceNotFoundException {
        return new ResponseEntity<>(productAssetService.createProductAsset(productAsset), HttpStatus.CREATED);
    }

}
