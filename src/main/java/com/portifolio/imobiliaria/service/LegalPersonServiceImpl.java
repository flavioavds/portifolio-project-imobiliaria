package com.portifolio.imobiliaria.service;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.portifolio.imobiliaria.dtos.person.CnpjDTORequest;
import com.portifolio.imobiliaria.dtos.person.CnpjDTOResponse;
import com.portifolio.imobiliaria.dtos.person.LegalPersonMapper;
import com.portifolio.imobiliaria.dtos.socio.SocioDTORequest;
import com.portifolio.imobiliaria.dtos.socio.SocioMapper;
import com.portifolio.imobiliaria.entities.LegalPerson;
import com.portifolio.imobiliaria.entities.Socio;
import com.portifolio.imobiliaria.exception.InvalidDocumentException;
import com.portifolio.imobiliaria.repositories.LegalPersonRepository;
import com.portifolio.imobiliaria.valid.ValidationCPF_CNPJ;

@Service
public class LegalPersonServiceImpl implements LegalPersonService {

	private final LegalPersonRepository repository;
    private final MessageSource messages;

    public LegalPersonServiceImpl(LegalPersonRepository repository, MessageSource messages) {
        super();
        this.repository = repository;
        this.messages = messages;
    }
    
    @Override
    public CnpjDTOResponse saveCnpj(CnpjDTORequest dto, Locale locale) {
    	LegalPerson legalPerson = LegalPersonMapper.fromDTO(dto);
    	
    	ValidationCPF_CNPJ validationCPF_CNPJ = new ValidationCPF_CNPJ();
		if (!validationCPF_CNPJ.isValidCNPJ(dto.getCnpj())) {
	    	throw new InvalidDocumentException(
	    			String.format(messages.getMessage("person.message.error-ivalid-cnpj", null, locale))
	    			);
	    }
		
		for (SocioDTORequest socioDTO : dto.getSocios()) {
	    	Socio socio = SocioMapper.fromDTO(socioDTO);
	        legalPerson.addSocio(socio);
	    }

	    LegalPerson savedLegalPerson = repository.save(legalPerson);

	    return LegalPersonMapper.fromEntity(savedLegalPerson);
    }

}
