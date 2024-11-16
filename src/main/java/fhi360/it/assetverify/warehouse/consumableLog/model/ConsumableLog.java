package fhi360.it.assetverify.warehouse.consumableLog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consumable_log")
public class ConsumableLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    private Long productConsumableId;
}
