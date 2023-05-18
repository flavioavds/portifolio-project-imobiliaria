package com.portifolio.imobiliaria.dtos.endereco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTORequest {
	
	private String cep;
	private String logradouro;
	private String complemento;
	private String numero;
	private String bairro;
	private String localidade;
	private String uf;

}
