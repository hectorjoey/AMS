package fhi360.it.assetverify.issueLog.dto;

import lombok.Data;

@Data
public class IssueLogDto {
    private long id;
    private String date;
    private String states;
    private String description;
    private String dateReceived;
    private String poNumber;
    private String vendor;
    private String unit;
    private String stockState;
    private String quantityReceived;
    private String issuedTo;
    private String quantityIssued;
    private String balance;
    private Long inventoryId;
    private Long userId;

    private String minimumStockLevel;
    private String category;
}
