package com.deephire.Controllers;

import com.deephire.JWT.JwtUtils;
import com.deephire.Enums.ERole;


import com.deephire.JWT.JwtUtils;
import com.deephire.Models.AdminCompany;
import com.deephire.Models.RefreshToken;
import com.deephire.Models.Role;
import com.deephire.Models.User;
import com.deephire.Repositories.RoleRepository;
import com.deephire.Repositories.UserRepository;
import com.deephire.Request.LogOutRequest;
import com.deephire.Request.LoginRequest;
import com.deephire.Request.SignupRequest;
import com.deephire.Response.JwtResponse;
import com.deephire.Service.MessageResponse;
import com.deephire.Service.RefreshTokenService;
import com.deephire.Service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class
AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    // Define endpoints for authentication and token management here


    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Message: user already exists"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        System.out.println(signupRequest);
        Set<String> strRoles = signupRequest.getRole();
        User user;

        // Decode base64 file if available
        byte[] decodedFile = null;
        if (signupRequest.getFile() != null && !signupRequest.getFile().isEmpty()) {
            decodedFile = Base64.getDecoder().decode(signupRequest.getFile());
        }

        // Check if ADMIN_COMPANY is among the roles
        if (strRoles.contains("ADMIN_COMPANY")) {
            user = new AdminCompany(
                    signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    encoder.encode(signupRequest.getPassword()),
                    signupRequest.getFirstname(),
                    signupRequest.getLastname(),
                    false,  // isValid set to false by default
                    decodedFile
            );
        } else {
            user = new User(
                    signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    encoder.encode(signupRequest.getPassword()),
                    signupRequest.getFirstname(),
                    signupRequest.getLastname()
            );
        }
        System.out.println(user.getLastName()+"111111111111111111111111111111111");

        user.setFirstLogin(true);

        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role not found")));
                        break;
                    case "ADMIN_COMPANY":
                        roles.add(roleRepository.findByName(ERole.ROLE_ADMINCOMPANY)
                                .orElseThrow(() -> new RuntimeException("Role not found")));
                        break;
                    default:
                        roles.add(roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role not found")));
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }





    @PostMapping(value="/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


            // Get the user entity
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String jwt = jwtUtils.generateJwtToken(userDetails);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());



            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            System.out.println("it is working until this point");
            return ResponseEntity.ok()
                    .body(new JwtResponse(
                            jwt,
                            refreshToken.getToken(),
                            userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles,
                            user.getFirstLogin()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Authentication failed"));
        }
    }




    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        System.out.println(user.getId()==null ? "null" : user.getId()+"5555555555555555555555555");
        refreshTokenService.deleteByUserId(user.getId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));


    }


}
