package com.portifolio.imobiliaria.service;

import java.util.Locale;

import com.portifolio.imobiliaria.dtos.person.CnpjDTORequest;
import com.portifolio.imobiliaria.dtos.person.CnpjDTOResponse;

public interface LegalPersonService {
	
	CnpjDTOResponse saveCnpj(CnpjDTORequest dto, Locale locale);

}
