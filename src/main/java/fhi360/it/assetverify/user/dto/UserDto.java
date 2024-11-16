
package fhi360.it.assetverify.user.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private long id;
    private String firstname;
    private String lastname;
    private String role;
    private String project;
    private String email;
    private String country;
    private String states;
    private String department;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
}
