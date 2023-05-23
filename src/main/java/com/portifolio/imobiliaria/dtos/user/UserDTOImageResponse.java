package com.portifolio.imobiliaria.dtos.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOImageResponse {
	
	private UUID id;
	private byte[] imagePerfil;

}
