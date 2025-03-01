package hu._ig.crm.crm4ig.controller;

import hu._ig.crm.crm4ig.constants.Constants;
import hu._ig.crm.crm4ig.model.AuthenticationRequest;
import hu._ig.crm.crm4ig.service.impl.JwtService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import static hu._ig.crm.crm4ig.constants.Constants.BEARER;
import static hu._ig.crm.crm4ig.constants.Constants.EMPTY_STRING;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "CRM 4IG Service", version = "v1"))
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final CsrfTokenRepository csrfTokenRepository;

    @Value("${angular.client.url}")
    private String angularClientUrl;

    @Operation(summary = "POST request for user token", description = "Generate new user token based on user details.")
    @CrossOrigin(origins = "${angular.client.url}")
    @PostMapping("/v1/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthenticationRequest request) {
        log.debug("**********/auth/v1/login***********");
        log.debug("request: " + request.getUsername() + " - " + request.getPassword() + request);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String jwt = jwtService.generateToken(authentication);
        if (jwt != null && !jwt.isBlank()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, String.join(EMPTY_STRING, BEARER, jwt));
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            var tokenBody = Map.of(Constants.ACCESS_TOKEN, jwt);
            return new ResponseEntity<>(tokenBody, httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "GET request for csrf token", description = "Generate new csrf token.")
    @CrossOrigin(origins = "${angular.client.url}")
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        log.debug("**********/auth/csrf-token***********");
        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(csrfToken, request, response);
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        log.debug("Returning CSRF Token: {}", csrfToken.getToken());
        return csrfToken;
    }
}
