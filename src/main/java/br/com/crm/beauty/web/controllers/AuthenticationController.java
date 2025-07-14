package br.com.crm.beauty.web.controllers;

import java.net.URI;

import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties.Problemdetails;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nimbusds.jose.shaded.gson.JsonObject;

import br.com.crm.beauty.web.dtos.ApiResponse;
import br.com.crm.beauty.web.dtos.AuthenticationDto;
import br.com.crm.beauty.web.dtos.JwtTokenDto;
import br.com.crm.beauty.web.dtos.UserDto;
import br.com.crm.beauty.web.models.User;
import br.com.crm.beauty.web.services.user.AuthenticationService;
import br.com.crm.beauty.web.services.user.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody AuthenticationDto auth) {
        var token = authenticationService.authenticate(auth);

        return ResponseEntity.ok(token);
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody @Validated UserDto user) {

        userService.add(user);

        var response = new ApiResponse<UserDto>("Success", 201,
                "An email has been sent to " + user.getEmail() + " for verification.", null);

        return ResponseEntity.ok().body(response);

    }
}
