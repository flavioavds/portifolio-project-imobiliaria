package com.portifolio.imobiliaria.dtos.person;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpfDTOResponse {
	
	private UUID id;
	private String name;
	private String cpf;

}
