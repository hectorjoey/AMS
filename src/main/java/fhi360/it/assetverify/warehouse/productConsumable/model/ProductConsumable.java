package fhi360.it.assetverify.warehouse.productConsumable.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_consumable")
public class ProductConsumable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String date;
    private String description;
    private String dateReceived;
    private String skuNumber;
    private String batchNo;
    private String expiryDate;
    private String manufacturedDate;
    private boolean hasExpired;
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

    private String lastModifiedBy;
    private String lastModifiedDate;
}


