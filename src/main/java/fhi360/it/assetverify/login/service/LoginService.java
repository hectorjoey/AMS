package fhi360.it.assetverify.login.service;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.login.dto.LoginDto;
import fhi360.it.assetverify.response.LoginResponse;
import fhi360.it.assetverify.user.model.Users;
import fhi360.it.assetverify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginResponse<Users> login(@Valid final LoginDto loginDto) throws ResourceNotFoundException {
        final Users user = userRepository.findByEmail(loginDto.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException(String.format("User with email %s does not exist.", loginDto.getEmail()));
        }
        if (!BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password Mismatch!.");
        }
        return new LoginResponse<>(200, "Login Success!", user, user.getId(), user.getRole(), user.getEmail(), user.getFirstname(), user.getLastname(), user.getStates(), user.getCountry(), user.getDepartment());
//    }
    }

//    public LoginResponse<Users> login(@Valid @RequestBody LoginDto loginDto) throws ResourceNotFoundException {
//        final Users user = userRepository.findByEmail(loginDto.getEmail());
//        if (user == null) {
//            throw new ResourceNotFoundException(String.format("User with email %s does not exist.", loginDto.getEmail()));
//        }
//        return new LoginResponse<>(200, "Login Success!", user, user.getId(), user.getRole(), user.getEmail(), user.getFirstname(), user.getLastname(), user.getStates(), user.getCountry(), user.getDepartment());
//    }



    public boolean isUserAlreadyPresent(final Users user) {
        boolean isUserAlreadyExists = false;
        final Users existingUser = this.userRepository.getByEmail(user.getEmail());
        if (existingUser != null) {
            isUserAlreadyExists = true;
        }
        return isUserAlreadyExists;
    }
}
