package fhi360.it.assetverify.inventory.dto;

import lombok.Data;

@Data
public class InventoryDto {
    private Long id;
    private String date;
    private String country;
    private String states;
    private String description;
    private String dateReceived;
    private String poNumber;
    private String vendor;
    private String unit;
    private String stockState;
    private String quantityReceived;
//    private String dateIssued;
//    private String issuedTo;
//    private String quantityIssued;
//    private String total;
    private String balance;
    private String minimumStockLevel;
    private String category;
}