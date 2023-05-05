package com.portifolio.imobiliaria.controllers;

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

import com.portifolio.imobiliaria.dtos.UserDTORequest;
import com.portifolio.imobiliaria.dtos.UserSignupDTOResponse;
import com.portifolio.imobiliaria.events.OnRegistrationSuccessEvent;
import com.portifolio.imobiliaria.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("v1/auth")
public class AuthController {
	
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

}
