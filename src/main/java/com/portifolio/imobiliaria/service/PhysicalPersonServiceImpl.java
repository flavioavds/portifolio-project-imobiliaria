package com.portifolio.imobiliaria.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.portifolio.imobiliaria.dtos.person.CpfDTORequest;
import com.portifolio.imobiliaria.dtos.person.CpfDTOResponse;
import com.portifolio.imobiliaria.dtos.person.PhysicalPersonMapper;
import com.portifolio.imobiliaria.entities.PhysicalPerson;
import com.portifolio.imobiliaria.exception.InvalidDocumentException;
import com.portifolio.imobiliaria.repositories.PhysicalPersonRepository;
import com.portifolio.imobiliaria.valid.ValidationCPF_CNPJ;

import jakarta.persistence.EntityNotFoundException;

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

	@Override
	public Page<CpfDTOResponse> findAll(Pageable pageable) {
		Page<PhysicalPerson> physicalPersonPage = repository.findAll(pageable);
		
		return physicalPersonPage.map(physicalPerson -> 
				{CpfDTOResponse cpfDTOResponse = PhysicalPersonMapper.cpfFromPhysicalPersonEntity(physicalPerson);
				return cpfDTOResponse;
				});
	}

	@Override
	public CpfDTOResponse findById(UUID id, Locale locale) {
		PhysicalPerson physicalPerson = physicalPersonById(id, locale);
		CpfDTOResponse cpfDTOResponse = PhysicalPersonMapper.cpfFromPhysicalPersonEntity(physicalPerson);
		return cpfDTOResponse;
	}
	
	private PhysicalPerson physicalPersonById(UUID id, Locale locale) {
		Optional<PhysicalPerson> optional = repository.findById(id);
		if(optional.isEmpty()) {
			throw new EntityNotFoundException(
					String.format(messages.getMessage("person.message.error-non-existent-entity-by-id", null, locale), id)
					);
		}
		return optional.get();
	}

	@Override
	public CpfDTOResponse update(UUID id, CpfDTORequest dto, Locale locale) {
		PhysicalPerson physicalPerson = physicalPersonById(id, locale);
		physicalPersonValidatedName(dto.getName(), locale);
		physicalPersonValidatedCPF(dto.getCpf(), locale);
		ValidationCPF_CNPJ validationCPF_CNPJ = new ValidationCPF_CNPJ();
		 if (!validationCPF_CNPJ.isValidCPF(dto.getCpf())) {
		        throw new InvalidDocumentException(
		                String.format(messages.getMessage("person.message.error-ivalid-cpf", null, locale))
		        );
		    }
		physicalPerson.setCpf(dto.getCpf());
		physicalPerson.setName(dto.getName());
		
		PhysicalPerson savedPhysicalPerson = repository.save(physicalPerson);
		CpfDTOResponse cpfDTOResponse = PhysicalPersonMapper.cpfFromPhysicalPersonEntity(savedPhysicalPerson);
		
		return cpfDTOResponse;
	}
	
	private void physicalPersonValidatedName(String name, Locale locale) {
		if(repository.findByNameIgnoreCase(name).isPresent()) {
			throw new DataIntegrityViolationException(
					String.format(messages.getMessage("person.message.error-already-registered", null, locale))
					);
		}
		new PhysicalPerson();
	}
	
	private void physicalPersonValidatedCPF(String cpf, Locale locale) {
		if(repository.findByCpf(cpf).isPresent()) {
			throw new DataIntegrityViolationException(
					String.format(messages.getMessage("person.message.error-cpf-already-registered", null, locale))
					);
		}
		new PhysicalPerson();
	}

	@Override
	public void deletePhysicalPerson(UUID id, Locale locale) {
		PhysicalPerson physicalPerson = physicalPersonById(id, locale);
		repository.delete(physicalPerson);
		
	}

	@Override
	public CpfDTOResponse findByCpf(String cpf, Locale locale) {
		Optional<PhysicalPerson> optional = repository.findByCpf(cpf);
		
		if (optional.isEmpty()) {
	        throw new EntityNotFoundException(
	            String.format(messages.getMessage("physicalperson.message.error-not-found-cpf", null, locale), cpf)
	        );
	    }
		
		PhysicalPerson physicalPerson = optional.get();
		CpfDTOResponse cpfDTOResponse = PhysicalPersonMapper.cpfFromPhysicalPersonEntity(physicalPerson);
		
		return cpfDTOResponse;
	}

	@Override
	public List<CpfDTOResponse> findByNameStartingWithIgnoreCase(String name, Locale locale) {
		List<PhysicalPerson> physicalPerson = repository.findByNameStartingWithIgnoreCase(name);
		
		return physicalPerson.stream()
				.map(this::mapPhysicalPersonToCpfDTOResponse)
				.collect(Collectors.toList());
	}
	
	private CpfDTOResponse mapPhysicalPersonToCpfDTOResponse(PhysicalPerson physicalPerson) {
		CpfDTOResponse cpfDTOResponse = new CpfDTOResponse();
		cpfDTOResponse.setId(physicalPerson.getId());
		cpfDTOResponse.setCpf(physicalPerson.getCpf());
		cpfDTOResponse.setName(physicalPerson.getName());
		return cpfDTOResponse;
	}

}
