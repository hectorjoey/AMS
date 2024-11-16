package fhi360.it.assetverify.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse<T> {
    private int status;
    private String message;
    private T result;
    private long id;
    private String role;
    private String email;
    private String firstname;
    private String lastname;
    private String states;
    private String country;
    private String department;
}
