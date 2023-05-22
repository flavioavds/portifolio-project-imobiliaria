package com.portifolio.imobiliaria.security.jwt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	 private String name;
	    private String email;
	    private String password;
	    private List<String> roles;
	    private String userType;
}
