package com.portifolio.imobiliaria.dtos.endereco;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.entities.Endereco;

public class EnderecoMapper {
	
	public static Endereco fromDTO(EnderecoDTORequest dto) {
		Endereco endereco = new Endereco();
		BeanUtils.copyProperties(dto, endereco);
		return endereco;
	}
	
	public static EnderecoDTOResponse fromEntity(Endereco endereco) {
		EnderecoDTOResponse enderecoDTOResponse = new EnderecoDTOResponse(
				endereco.getCep(), 
				endereco.getLogradouro(), 
				endereco.getComplemento(), 
				endereco.getNumero(), 
				endereco.getBairro(), 
				endereco.getLocalidade(), 
				endereco.getUf());
		
		return enderecoDTOResponse;
	}

}
