package com.portifolio.imobiliaria.dtos.endereco;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {
	
	private UUID id;
	private String cep;
	private String logradouro;
	private String complemento;
	private Integer numero;
	private String bairro;
	private String localidade;
	private String uf;

}
