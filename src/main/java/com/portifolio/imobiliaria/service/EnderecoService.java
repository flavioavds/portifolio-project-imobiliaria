package com.portifolio.imobiliaria.service;

import java.util.Locale;

import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTORequest;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTOResponse;

public interface EnderecoService {
	
	EnderecoDTOResponse saveEndereco(EnderecoDTORequest dto, Locale locale);
	
}
