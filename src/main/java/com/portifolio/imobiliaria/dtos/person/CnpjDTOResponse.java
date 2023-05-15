package com.portifolio.imobiliaria.dtos.person;

import java.util.List;
import java.util.UUID;

import com.portifolio.imobiliaria.entities.Socio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CnpjDTOResponse {
	
	private UUID id;
	private String name;
	private String cnpj;
	private List<Socio> socios;

}
