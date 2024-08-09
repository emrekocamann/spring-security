package com.emre.security.jwt_token.controller;

import com.emre.security.jwt_token.dto.AuthRequest;
import com.emre.security.jwt_token.dto.CreateUserRequest;
import com.emre.security.jwt_token.model.User;
import com.emre.security.jwt_token.service.JwtService;
import com.emre.security.jwt_token.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    private  final UserService service;
    private  final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Hello World !";
    }
    @PostMapping("/addNewUser")
    public User addUser(@RequestBody CreateUserRequest request){
        return service.createUser(request);

    }

    @GetMapping("/user")
    public String getUserString(){
        return "This is USER!";
    }

    @GetMapping("/admin")
    public String getAdminString(){
        return "This is ADMIN!";
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(request.username());
        }
        log.info("Invalid username"+request.username());
        throw new UsernameNotFoundException("Invalid Credentials");
    }
}

