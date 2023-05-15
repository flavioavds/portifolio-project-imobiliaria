package com.portifolio.imobiliaria.dtos.socio;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioDTOResponse {
	
	private UUID id;
	private String name;
	private String cpf;

}
