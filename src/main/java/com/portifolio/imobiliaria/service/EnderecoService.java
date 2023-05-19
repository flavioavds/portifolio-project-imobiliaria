package com.portifolio.imobiliaria.service;

import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTORequest;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTOResponse;

public interface EnderecoService {
	
	EnderecoDTOResponse saveEndereco(EnderecoDTORequest dto, Locale locale);
	Page<EnderecoDTOResponse> findAll(Pageable pageable);
	EnderecoDTOResponse findById(UUID id, Locale locale);
	EnderecoDTOResponse update(UUID id, EnderecoDTORequest dto, Locale locale);
	void deleteEndereco(UUID id, Locale locale);
	EnderecoDTOResponse findByCep(String cep, Locale locale);
	Page<EnderecoDTOResponse> findByLogradouroStartingWithIgnoreCase(String logradouro, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByComplementoStartingWithIgnoreCase(String complemento, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByNumeroStartingWithIgnoreCase(String numero, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByNumeroBetween(String numero, Integer numeroInicial, Integer numeroFinal, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByNumeroLessThanOrEqual(String numero, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByNumeroGreaterThanOrEqual(String numero, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByBairroStartingWithIgnoreCase(String bairro, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByLocalidadeStartingWithIgnoreCase(String localidade, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByUfStartingWithIgnoreCase(String uf, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByLocalidadeAndBairroStartingWithIgnoreCase(String localidade, String bairro, Locale locale, Pageable pageable);
	Page<EnderecoDTOResponse> findByLogradouroAndBairroStartingWithIgnoreCase(String logradouro, String bairro, Locale locale, Pageable pageable);
}
