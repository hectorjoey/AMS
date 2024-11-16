package fhi360.it.assetverify.warehouse.productAsset.dto;

import lombok.Data;

@Data
public class ProductAssetDto {
    private long id;
    private String dateReceived;
    private String description;
    private String manufacturer;
    private String skuNumber;
    private String poNumber;
    private String quantityReceived;
    private String purchasePrice;
    private String funder;
    private String country;
    private String states;
    private String lastModifiedBy;
    private String lastUpdatedDate;
}
