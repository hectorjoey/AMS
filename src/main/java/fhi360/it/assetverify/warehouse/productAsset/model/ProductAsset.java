package fhi360.it.assetverify.warehouse.productAsset.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_asset")
public class ProductAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "dateReceived")
    private String dateReceived;

    @Column(name = "description")
    private String description;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "skuNumber")
    private String skuNumber;

    @Column(name = "poNumber")
    private String poNumber;

    @Column(name = "quantityReceived")
    private String quantityReceived;

    @Column(name = "purchasePrice")
    private String purchasePrice;

//    @Column(name = "depreciationValue")
//    private String depreciationValue;

    @Column(name = "funder")
    private String funder;

    @Column(name = "country")
    private String country;

    @Column(name = "states")
    private String states;

    @Column(name = "approvedBy")
    private String approvedBy;

    @Column(name = "lastModifiedBy")
    private String lastModifiedBy;

    @Column(name = "lastUpdatedDate")
    private String lastUpdatedDate;
}
