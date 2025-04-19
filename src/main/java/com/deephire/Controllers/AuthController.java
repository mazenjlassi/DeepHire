package com.deephire.Controllers;

import com.deephire.JWT.JwtUtils;
import com.deephire.Enums.ERole;


import com.deephire.JWT.JwtUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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


    @PostMapping(value = "/signup",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Message user already exists "));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail()))  {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error :Email is already in use"));
        }


        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();



        if (strRoles == null || strRoles.isEmpty()) {
            // Si aucun rôle n'est spécifié, on met le rôle USER par défaut
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(adminRole);
                        break;
                    case "ADMINCOMPANY":
                        Role modRole = roleRepository.findByName(ERole.ROLE_ADMINCOMPANY)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
//    @PostMapping(value="/signin",produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
//        System.out.println(loginRequest.getPassword()+"000000000000000000000");
//        try {
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUsername(),
//                            loginRequest.getPassword()));
//
//
//            System.out.println("//////////////////////////");
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//            System.out.println(userDetails+"+++++++++++++++++++++++++++++++++");
//
//
//            String jwt = jwtUtils.generateJwtToken(userDetails);
//
//            System.out.println(jwt+"/////////////////////////////////////////////");
//
//            List<String> roles = userDetails.getAuthorities()
//                    .stream()
//                    .map(item -> item.getAuthority())
//                    .collect(Collectors.toList());
//
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
//
//            return ResponseEntity.ok(new JwtResponse(
//                    jwt,
//                    refreshToken.getToken(),
//                    userDetails.getId(),
//                    userDetails.getUsername(),
//                    userDetails.getEmail(),
//                    roles));
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new MessageResponse("Invalid username or password"));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("Authentication failed: " + e.getMessage()));
//        }
//    }



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
                            roles
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Authentication failed"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }


}
