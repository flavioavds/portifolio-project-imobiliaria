package com.portifolio.imobiliaria.controllers;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portifolio.imobiliaria.dtos.user.UserDTORequest;
import com.portifolio.imobiliaria.dtos.user.UserSignupDTOResponse;
import com.portifolio.imobiliaria.events.OnRegistrationSuccessEvent;
import com.portifolio.imobiliaria.security.auth.AuthenticationRequest;
import com.portifolio.imobiliaria.security.auth.AuthenticationResponse;
import com.portifolio.imobiliaria.security.config.service.AuthenticationService;
import com.portifolio.imobiliaria.security.jwt.RegisterRequest;
import com.portifolio.imobiliaria.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("v1/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private UserService service;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSignupDTOResponse> create(@RequestBody UserDTORequest dto,
                                                        @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale,
                                                        HttpServletRequest request) {
        UserSignupDTOResponse registeredUser = service.create(dto, new Locale(locale));
        eventPublisher.publishEvent(new OnRegistrationSuccessEvent(registeredUser,  new Locale(locale), request.getContextPath()));
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
	
	@PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest, 
    													   @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale,
    													   HttpServletRequest request) {		
        AuthenticationResponse registeredUser = authenticationService.register(registerRequest, new Locale(locale));
        eventPublisher.publishEvent(new OnRegistrationSuccessEvent(registeredUser,  new Locale(locale), request.getContextPath()));
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
	
	@PostMapping("/authenticate")
	  public ResponseEntity<AuthenticationResponse> authenticate(
	      @RequestBody AuthenticationRequest request
	  ) {
	    return ResponseEntity.ok(authenticationService.authenticate(request));
	  }

	  @PostMapping("/refresh-token")
	  public void refreshToken(
	      HttpServletRequest request,
	      HttpServletResponse response
	  ) throws IOException {
		  authenticationService.refreshToken(request, response);
	  }

}
