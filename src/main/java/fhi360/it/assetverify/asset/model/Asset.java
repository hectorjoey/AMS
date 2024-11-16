package fhi360.it.assetverify.asset.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "assets")
public class Asset implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "assetId")
    private String assetId;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "modelNumber")
    private String modelNumber;

    @Column(name = "serialNumber")
    private String serialNumber;

    @Column(name = "poNumber")
    private String poNumber;

    @Column(name = "dateReceived")
    private String dateReceived;

    @Column(name = "purchasePrice")
    private String purchasePrice;

    @Column(name = "currentAgeOfAsset")
    private String currentAgeOfAsset;

    @Column(name = "condition")
    private String condition;

    @Column(name = "country")
    private String country;

    @Column(name = "states")
    private String states;

    @Column(name = "emailAddress")
    private String emailAddress;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private String status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "approvedBy")
    private String approvedBy;

    @Column(name = "lastModifiedBy")
    private String lastModifiedBy;

    @Column(name = "lastUpdatedDate")
    private String lastUpdatedDate;

}