package fhi360.it.assetverify.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssetDto {
    private String description;
    private String assetId;
    private String manufacturer;
    private String modelNumber;
    private String serialNumber;
    private String poNumber;
    private String dateReceived;
    private String purchasePrice;
    private String currentAgeOfAsset;
    private String condition;
    private String country;
    private String states;
    private String emailAddress;
    private String assignee;
    private String phone;
    private String status;
    private String comment;
    private String approvedBy;
    private String lastModifiedBy;
    private String lastUpdatedDate;
}
