package com.portifolio.imobiliaria.dtos.person;

import java.util.List;

import com.portifolio.imobiliaria.dtos.socio.SocioDTORequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CnpjDTORequest {
	
	private String name;
	private String cnpj;
	private List<SocioDTORequest> socios;

}
