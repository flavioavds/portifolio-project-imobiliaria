package com.portifolio.imobiliaria.dtos.socio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioDTORequest {
	
	private String name;
	private String cpf;

}
