package com.portifolio.imobiliaria.service;

import java.util.Locale;

import com.portifolio.imobiliaria.dtos.UserDTORequest;
import com.portifolio.imobiliaria.dtos.UserSignupDTOResponse;

public interface UserService {
	
	UserSignupDTOResponse create(UserDTORequest userDTORequest, Locale locale);

}
