package com.portifolio.imobiliaria.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.portifolio.imobiliaria.dtos.person.CpfDTORequest;
import com.portifolio.imobiliaria.dtos.person.CpfDTOResponse;

public interface PhysicalPersonService {
	
	CpfDTOResponse saveCpf(CpfDTORequest dto, Locale locale);
	Page<CpfDTOResponse> findAll(Pageable pageable);
	CpfDTOResponse findById(UUID id, Locale locale);
	CpfDTOResponse update(UUID id, CpfDTORequest dto, Locale locale);
	void deletePhysicalPerson(UUID id, Locale locale);
	CpfDTOResponse findByCpf(String cpf, Locale locale);
	List<CpfDTOResponse> findByNameStartingWithIgnoreCase(String name, Locale locale);

}
