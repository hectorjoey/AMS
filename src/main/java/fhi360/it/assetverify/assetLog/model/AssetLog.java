package fhi360.it.assetverify.assetLog.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="assetLog")
public class AssetLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private String date;

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

    @Column(name = "condition")
    private String condition;

    @Column(name = "country")
    private String country;

    @Column(name = "states")
    private String states;

    @Column(name = "Assignee")
    private String assignee;

    @Column(name = "emailAddress")
    private String emailAddress;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private String status;

    @Column(name = "lastModifiedBy")
    private String lastModifiedBy;

    @Column(name = "lastUpdatedDate")
    private String lastUpdatedDate;

    private String approvedBy;
    private String comment;

}
