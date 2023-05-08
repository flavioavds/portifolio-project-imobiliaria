package com.portifolio.imobiliaria.dtos.user;

import java.util.List;
import java.util.UUID;

import com.portifolio.imobiliaria.entities.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupDTOResponse {
	
	private UUID id;	
	private String name;
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	private List<Role> roles;

}
