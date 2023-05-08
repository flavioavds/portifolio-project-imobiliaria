package com.portifolio.imobiliaria.controllers;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portifolio.imobiliaria.dtos.user.UpdateUserDTO;
import com.portifolio.imobiliaria.dtos.user.UserDTOResponse;
import com.portifolio.imobiliaria.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	private final UserService service;

    public UserController(UserService service) {
		this.service = service;
	}
    
    @GetMapping
    public ResponseEntity<List<UserDTOResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findAll());
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTOResponse> findById(@PathVariable UUID id,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findById(id, new Locale(locale)));
    }
    
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<UserDTOResponse> updateUser(@RequestBody UpdateUserDTO dto,
                                                      @PathVariable UUID id,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.update(id, dto, new Locale(locale)));
    }
    
    @DeleteMapping(value = "/{id}/inactivate")
    public ResponseEntity<Void> inactivate(@PathVariable UUID id,
                                           @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
        service.inactivate(id, new Locale(locale));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @PutMapping(value = "/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
        service.activate(id, new Locale(locale));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UserDTOResponse> findByEmail(@PathVariable String email,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findByEmail(email, new Locale(locale)));
    }
    
}
