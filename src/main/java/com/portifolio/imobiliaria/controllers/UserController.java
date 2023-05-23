package com.portifolio.imobiliaria.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portifolio.imobiliaria.dtos.user.UpdateUserDTO;
import com.portifolio.imobiliaria.dtos.user.UserDTOImageResponse;
import com.portifolio.imobiliaria.dtos.user.UserDTOResponse;
import com.portifolio.imobiliaria.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
//	@Autowired
//	private UserServiceImpl serviceImpl;

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
    
    @PutMapping("/{userId}/profile-image")
    public ResponseEntity<UserDTOImageResponse> updateProfileImage(
            @PathVariable UUID userId,
            @RequestParam("image") MultipartFile image,
            Locale locale) {
        
        try {
            byte[] imageBytes = image.getBytes();
            UserDTOImageResponse response = service.updateProfileImage(userId, imageBytes, locale);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Resource> getProfileImage(@PathVariable UUID userId) {
        byte[] imageBytes = service.getProfileImage(userId);

        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        return ResponseEntity.ok()
                .contentLength(imageBytes.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
    
}
