package fhi360.it.assetverify.verification.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "verification")
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private String date;
    private Boolean sightedStatus;
    private String assetId;

}
