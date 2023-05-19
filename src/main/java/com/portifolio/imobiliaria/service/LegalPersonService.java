package com.portifolio.imobiliaria.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.portifolio.imobiliaria.dtos.person.CnpjDTORequest;
import com.portifolio.imobiliaria.dtos.person.CnpjDTOResponse;
import com.portifolio.imobiliaria.dtos.socio.SocioDTORequest;
import com.portifolio.imobiliaria.dtos.socio.SocioDTOResponse;
import com.portifolio.imobiliaria.entities.LegalPerson;
import com.portifolio.imobiliaria.entities.Socio;

public interface LegalPersonService {
	
	CnpjDTOResponse saveCnpj(CnpjDTORequest dto, Locale locale);
	Page<CnpjDTOResponse> findAll(Pageable pageable);
	CnpjDTOResponse findById(UUID id, Locale locale);
	LegalPerson findByEntity(UUID id, Locale locale);
	CnpjDTOResponse update(UUID id, CnpjDTORequest dto, Locale locale);
	CnpjDTOResponse addSocio(UUID legalPersonId, SocioDTORequest socioDTO, Locale locale);
	void deleteSocio(UUID legalPersonId, UUID socioId, Locale locale);
	void deleteLegalPerson(UUID id, Locale locale);
	SocioDTOResponse findSocioByCpf(UUID legalPersonId, String cpf, Locale locale);
	List<Socio> findBySociosNameStartingWithIgnoreCase(String name, Locale locale);
	CnpjDTOResponse findByCnpj(String cnpj, Locale locale);
	List<CnpjDTOResponse> findByNameStartingWithIgnoreCase(String name, Locale locale);
	

}
