package com.deephire.Controllers;

import com.deephire.Config.UserAuthenticationProvider;
import com.deephire.Dto.CredentialsDto;
import com.deephire.Dto.LoginResponseDto;
import com.deephire.Dto.SignUpDto;
import com.deephire.Dto.UserDto;
import com.deephire.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {


     @Autowired
    private  UserService userService;
     @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        String token = userAuthenticationProvider.createToken(userDto);
        return ResponseEntity.ok(LoginResponseDto.builder()
                .token(token)
                .user(userDto)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto user = userService.register(signUpDto);
        // Constructing a valid URI for the created user. Make sure the path matches your endpoint design.
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
