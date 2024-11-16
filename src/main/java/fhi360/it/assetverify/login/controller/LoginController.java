package fhi360.it.assetverify.login.controller;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.login.dto.LoginDto;
import fhi360.it.assetverify.login.service.LoginService;
import fhi360.it.assetverify.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping({"/api/v1"})
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping({"/login"})
    public LoginResponse login(@Valid @RequestBody final LoginDto loginDto) throws ResourceNotFoundException {
        return this.loginService.login(loginDto);
    }

    @PostMapping({"/android-login"})
    public LoginResponse androidLogin(@Valid final LoginDto loginDto) throws ResourceNotFoundException {
        return this.loginService.login(loginDto);
    }
}
