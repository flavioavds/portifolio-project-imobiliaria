package com.portifolio.imobiliaria.dtos.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpfDTORequest {

	private String name;
	private String cpf;
	
}
