package fhi360.it.assetverify.warehouse.productConsumable.dto;

import lombok.Data;

@Data
public class ProductConsumableDto {
    private long id;
    private String date;
    private String description;
    private String dateReceived;
    private String skuNumber;
    private String batchNo;
    private String country;
    private String states;
    private String dateIssued;
    private String poNumber;
    private String vendor;
    private String unit;
    private String stockState;
    private String quantityReceived;
    private String issuedTo;
    private String quantityIssued;
    private String balance;
    private String minStockLevel;
    private String category;
}
