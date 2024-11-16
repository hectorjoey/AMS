
package fhi360.it.assetverify.login.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
