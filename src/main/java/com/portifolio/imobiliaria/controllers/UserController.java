package com.portifolio.imobiliaria.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portifolio.imobiliaria.dtos.UserDTOResponse;
import com.portifolio.imobiliaria.entities.User;
import com.portifolio.imobiliaria.service.UserService;
import com.portifolio.imobiliaria.service.UserServiceImpl;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	@Autowired
	private UserService service;

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

//    @GetMapping
//    public List<User> getAllUsers() {
//        return userServiceImpl.getAllUsers();
//    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userServiceImpl.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userServiceImpl.saveUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userServiceImpl.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userServiceImpl.deleteUser(id);
    }
    
    @GetMapping
    public ResponseEntity<List<UserDTOResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findAll());
    }
}
