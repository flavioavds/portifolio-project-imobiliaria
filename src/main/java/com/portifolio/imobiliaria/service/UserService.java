package com.portifolio.imobiliaria.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.portifolio.imobiliaria.dtos.user.UpdateUserDTO;
import com.portifolio.imobiliaria.dtos.user.UserDTORequest;
import com.portifolio.imobiliaria.dtos.user.UserDTOResponse;
import com.portifolio.imobiliaria.dtos.user.UserSignupDTOResponse;
import com.portifolio.imobiliaria.entities.User;

public interface UserService {
	
	UserSignupDTOResponse create(UserDTORequest userDTORequest, Locale locale);
	List<UserDTOResponse> findAll();
	UserDTOResponse findById(UUID id, Locale locale);
	User findByIdAuth(UUID id, Locale locale);
	UserDTOResponse update(UUID id, UpdateUserDTO dto, Locale locale);
	void inactivate(UUID id, Locale locale);    
    void activate(UUID id, Locale locale);
}
