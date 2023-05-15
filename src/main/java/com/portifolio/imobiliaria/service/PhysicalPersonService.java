package com.portifolio.imobiliaria.service;

import java.util.Locale;

import com.portifolio.imobiliaria.dtos.person.CpfDTORequest;
import com.portifolio.imobiliaria.dtos.person.CpfDTOResponse;

public interface PhysicalPersonService {
	
	CpfDTOResponse saveCpf(CpfDTORequest dto, Locale locale);

}
