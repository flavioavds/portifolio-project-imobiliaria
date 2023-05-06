package com.portifolio.imobiliaria.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.portifolio.imobiliaria.dtos.UserDTORequest;
import com.portifolio.imobiliaria.dtos.UserDTOResponse;
import com.portifolio.imobiliaria.dtos.UserSignupDTOResponse;

public interface UserService {
	
	UserSignupDTOResponse create(UserDTORequest userDTORequest, Locale locale);
	List<UserDTOResponse> findAll();
	UserDTOResponse findById(UUID id, Locale locale);

}
