package com.portifolio.imobiliaria.dtos.endereco;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.entities.Endereco;

public class EnderecoMapper {
	
	public static Endereco fromDTO(EnderecoDTORequest dto) {
        Endereco endereco = new Endereco();
        BeanUtils.copyProperties(dto, endereco, "id");
        return endereco;
    }

	public static EnderecoDTOResponse fromEntity(Endereco endereco) {
	    EnderecoDTOResponse enderecoDTOResponse = new EnderecoDTOResponse();
	    enderecoDTOResponse.setId(endereco.getId());
	    BeanUtils.copyProperties(endereco, enderecoDTOResponse);
	    return enderecoDTOResponse;
	}

}
