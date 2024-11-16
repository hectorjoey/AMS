package fhi360.it.assetverify.inventory.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String date;
    private String country;
    private String states;
    private String description;
    private String dateReceived;
    private String dateIssued;
    private String poNumber;
    private String vendor;
    private String unit;
    private String stockState;
    private String quantityReceived;
    private String issuedTo;
    private String quantityIssued;
    private String balance;
    private String minimumStockLevel;
    private String category;
   }
