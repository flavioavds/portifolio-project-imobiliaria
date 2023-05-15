package com.portifolio.imobiliaria.service;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.portifolio.imobiliaria.dtos.person.CpfDTORequest;
import com.portifolio.imobiliaria.dtos.person.CpfDTOResponse;
import com.portifolio.imobiliaria.dtos.person.PhysicalPersonMapper;
import com.portifolio.imobiliaria.entities.PhysicalPerson;
import com.portifolio.imobiliaria.exception.InvalidDocumentException;
import com.portifolio.imobiliaria.repositories.PhysicalPersonRepository;
import com.portifolio.imobiliaria.valid.ValidationCPF_CNPJ;

@Service
public class PhysicalPersonServiceImpl implements PhysicalPersonService{
	
	private final PhysicalPersonRepository repository;
	private final MessageSource messages;

	public PhysicalPersonServiceImpl(PhysicalPersonRepository repository, MessageSource messages) {
		super();
		this.repository = repository;
		this.messages = messages;
	}

	@Override
	public CpfDTOResponse saveCpf(CpfDTORequest dto, Locale locale) {
		ValidationCPF_CNPJ validationCPF_CNPJ = new ValidationCPF_CNPJ();
		if (!validationCPF_CNPJ.isValidCPF(dto.getCpf())) {
	    	throw new InvalidDocumentException(
	    			String.format(messages.getMessage("person.message.error-ivalid-cpf", null, locale))
	    			);
	    }
	    return PhysicalPersonMapper.cpfFromPhysicalPersonEntity(repository.save(cpfToEntity(dto, locale)));
	}
	
	private PhysicalPerson cpfToEntity(CpfDTORequest dto, Locale locale) {
		PhysicalPerson person = PhysicalPersonMapper.fromPersonDTOCpf(dto);
		return person;
	}

}
