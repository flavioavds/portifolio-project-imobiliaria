package com.portifolio.imobiliaria.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTORequest {
	
	private String name;
	private String email;
	private String password;
	private List<String> roles;
	private String userType;
	
}
