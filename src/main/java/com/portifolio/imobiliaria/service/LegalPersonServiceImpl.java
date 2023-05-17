package com.portifolio.imobiliaria.service;

import java.util.ArrayList;
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

import com.portifolio.imobiliaria.dtos.person.CnpjDTORequest;
import com.portifolio.imobiliaria.dtos.person.CnpjDTOResponse;
import com.portifolio.imobiliaria.dtos.person.LegalPersonMapper;
import com.portifolio.imobiliaria.dtos.socio.SocioDTORequest;
import com.portifolio.imobiliaria.dtos.socio.SocioDTOResponse;
import com.portifolio.imobiliaria.dtos.socio.SocioMapper;
import com.portifolio.imobiliaria.entities.LegalPerson;
import com.portifolio.imobiliaria.entities.Socio;
import com.portifolio.imobiliaria.exception.InvalidDocumentException;
import com.portifolio.imobiliaria.repositories.LegalPersonRepository;
import com.portifolio.imobiliaria.repositories.SocioRepository;
import com.portifolio.imobiliaria.valid.ValidationCPF_CNPJ;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LegalPersonServiceImpl implements LegalPersonService {
	
	private final LegalPersonRepository repository;
	private final SocioRepository socioRepository;
    private final MessageSource messages;

	public LegalPersonServiceImpl(LegalPersonRepository repository, SocioRepository socioRepository,
			MessageSource messages) {
		super();
		this.repository = repository;
		this.socioRepository = socioRepository;
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
	    
	    List<Socio> socios = new ArrayList<>();

	    for (SocioDTORequest socioDTO : dto.getSocios()) {
	        if (!validationCPF_CNPJ.isValidCPF(socioDTO.getCpf())) {
	            throw new InvalidDocumentException(
	                    String.format(messages.getMessage("person.message.error-ivalid-cpf", null, locale))
	            );
	        }
	        Socio socio = SocioMapper.fromDTO(socioDTO);
	        socio.setLegalPerson(legalPerson);
	        socios.add(socio);
	    }

	    legalPerson.setSocios(socios);

	    LegalPerson savedLegalPerson = repository.save(legalPerson);

	    CnpjDTOResponse cnpjDTOResponse = LegalPersonMapper.fromEntity(savedLegalPerson);
	    cnpjDTOResponse.setSocios(socios.stream()
	            .map(SocioMapper::toDTO)
	            .collect(Collectors.toList()));

	    return cnpjDTOResponse;
	}
	
	@Override
	public Page<CnpjDTOResponse> findAll(Pageable pageable) {
	    Page<LegalPerson> legalPersonPage = repository.findAll(pageable);
	    
	    return legalPersonPage.map(legalPerson -> {
	        CnpjDTOResponse cnpjDTOResponse = LegalPersonMapper.fromEntity(legalPerson);
	        
	        List<SocioDTOResponse> socioDTOs = legalPerson.getSocios().stream()
	            .map(SocioMapper::fromEntity)
	            .collect(Collectors.toList());
	        
	        cnpjDTOResponse.setSocios(socioDTOs);
	        
	        return cnpjDTOResponse;
	    });
	}
	
	@Override
	public CnpjDTOResponse findById(UUID id, Locale locale) {
	    LegalPerson legalPerson = legalPersonVerifyById(id, locale);

	    CnpjDTOResponse cnpjDTOResponse = LegalPersonMapper.fromEntity(legalPerson);
	    
	    List<SocioDTOResponse> socioDTOs = legalPerson.getSocios().stream()
	            .map(SocioMapper::fromEntity)
	            .collect(Collectors.toList());
	    
	    cnpjDTOResponse.setSocios(socioDTOs);
	    
	    return cnpjDTOResponse;
	}

	private LegalPerson legalPersonVerifyById(UUID id, Locale locale) {
	    Optional<LegalPerson> optional = repository.findById(id);
	    if (optional.isEmpty()) {
	        throw new EntityNotFoundException(
	                String.format(messages.getMessage("person.message.error-non-existent-entity-by-id", null, locale), id)
	        );
	    }
	    return optional.get();
	}


	@Override
	public LegalPerson findByEntity(UUID id, Locale locale) {
		return legalPersonVerifyById(id, locale);
	}

	@Override
	public CnpjDTOResponse update(UUID id, CnpjDTORequest dto, Locale locale) {
	    LegalPerson legalPerson = legalPersonVerifyById(id, locale);
	    legalPersonValidatedName(dto.getName(), locale);
	    legalPersonValidatedCNPJ(dto.getCnpj(), locale);
	    legalPerson.setName(dto.getName());

	    ValidationCPF_CNPJ validationCPF_CNPJ = new ValidationCPF_CNPJ();
	    if (!validationCPF_CNPJ.isValidCNPJ(dto.getCnpj())) {
	        throw new InvalidDocumentException(
	                String.format(messages.getMessage("person.message.error-ivalid-cnpj", null, locale))
	        );
	    }
	    legalPerson.setCnpj(dto.getCnpj());

	    List<Socio> updatedSocios = new ArrayList<>();

	    for (SocioDTORequest socioDTO : dto.getSocios()) {
	        if (!validationCPF_CNPJ.isValidCPF(socioDTO.getCpf())) {
	            throw new InvalidDocumentException(
	                    String.format(messages.getMessage("person.message.error-ivalid-cpf", null, locale))
	            );
	        }

	        Socio existingSocio = legalPerson.findSocioByCpf(socioDTO.getCpf());

	        if (existingSocio != null) {
	            // Atualiza os dados do sócio existente
	            existingSocio.setName(socioDTO.getName());
	            updatedSocios.add(existingSocio);
	        } else {
	            // Cria um novo sócio e adiciona à lista de sócios atualizados
	            Socio newSocio = SocioMapper.fromDTO(socioDTO);
	            newSocio.setLegalPerson(legalPerson);
	            updatedSocios.add(newSocio);
	        }
	    }

	    // Define a lista atualizada de sócios
	    legalPerson.getSocios().clear();
	    legalPerson.getSocios().addAll(updatedSocios);

	    LegalPerson savedLegalPerson = repository.save(legalPerson);

	    CnpjDTOResponse cnpjDTOResponse = LegalPersonMapper.fromEntity(savedLegalPerson);

	    List<SocioDTOResponse> socioDTOs = savedLegalPerson.getSocios().stream()
	            .map(SocioMapper::fromEntity)
	            .collect(Collectors.toList());

	    cnpjDTOResponse.setSocios(socioDTOs);

	    return cnpjDTOResponse;
	}
	
	private void legalPersonValidatedName(String name, Locale locale) {
		if(repository.findByNameIgnoreCase(name).isPresent()) {
			throw new DataIntegrityViolationException(
					String.format(messages.getMessage("person.message.error-already-registered", null, locale))
					);
		}
		new LegalPerson();
	}
	
	private void legalPersonValidatedCNPJ(String cnpj, Locale locale) {
		if(repository.findByCnpj(cnpj).isPresent()) {
			throw new DataIntegrityViolationException(
					String.format(messages.getMessage("person.message.error-cnpj-already-registered", null, locale))
					);
		}
		new LegalPerson();
	}

	@Override
	public void deleteSocio(UUID legalPersonId, UUID socioId, Locale locale) {
	    LegalPerson legalPerson = legalPersonVerifyById(legalPersonId, locale);
	    Socio socio = legalPerson.findSocioById(socioId);

	    if (socio == null) {
	        throw new EntityNotFoundException(
	                String.format(messages.getMessage("socio.message.error-non-existent-entity-by-id", null, locale), socioId)
	        );
	    }

	    legalPerson.removeSocio(socio);
	    repository.save(legalPerson);
	}

	@Override
    public void deleteLegalPerson(UUID id, Locale locale) {
        LegalPerson legalPerson = legalPersonVerifyById(id, locale);
        repository.delete(legalPerson);
    }

	@Override
    public SocioDTOResponse findSocioByCpf(UUID legalPersonId, String cpf, Locale locale) {
        LegalPerson legalPerson = legalPersonVerifyById(legalPersonId, locale);
        Socio socio = legalPerson.findSocioByCpf(cpf);

        if (socio == null) {
            throw new EntityNotFoundException(
                    String.format("Socio com CPF %s não encontrado para LegalPerson com ID %s.", cpf, legalPersonId)
            );
        }

        return SocioMapper.fromEntity(socio);
    }

	@Override
	public List<Socio> findBySociosNameStartingWithIgnoreCase(String name, Locale locale) {
	    List<Socio> socios = socioRepository.findByNameStartingWithIgnoreCase(name);
	    
	    if (socios.isEmpty()) {
	        throw new EntityNotFoundException(
	            String.format(messages.getMessage("socio.message.error-not-found", null, locale), name)
	        );
	    }
	    
	    return socios;
	}

	@Override
	public CnpjDTOResponse findByCnpj(String cnpj, Locale locale) {
	    Optional<LegalPerson> optional = repository.findByCnpj(cnpj);
	    
	    if (optional.isEmpty()) {
	        throw new EntityNotFoundException(
	            String.format(messages.getMessage("legalperson.message.error-not-found-cnpj", null, locale), cnpj)
	        );
	    }
	    
	    LegalPerson legalPerson = optional.get();
	    
	    CnpjDTOResponse cnpjDTOResponse = LegalPersonMapper.fromEntity(legalPerson);

	    List<SocioDTOResponse> socioDTOs = legalPerson.getSocios().stream()
	            .map(SocioMapper::fromEntity)
	            .collect(Collectors.toList());

	    cnpjDTOResponse.setSocios(socioDTOs);
	    
	    return cnpjDTOResponse;
	}


	@Override
	public List<CnpjDTOResponse> findByNameStartingWithIgnoreCase(String name, Locale locale) {
        List<LegalPerson> legalPersons = repository.findByNameStartingWithIgnoreCase(name);

        return legalPersons.stream()
                .map(this::mapLegalPersonToCnpjDTOResponse)
                .collect(Collectors.toList());
    }

	private CnpjDTOResponse mapLegalPersonToCnpjDTOResponse(LegalPerson legalPerson) {
	    CnpjDTOResponse cnpjDTOResponse = new CnpjDTOResponse();
	    cnpjDTOResponse.setId(legalPerson.getId());
	    cnpjDTOResponse.setName(legalPerson.getName());
	    cnpjDTOResponse.setCnpj(legalPerson.getCnpj());

	    List<SocioDTOResponse> socioDTOs = legalPerson.getSocios().stream()
	            .map(SocioMapper::fromEntity)
	            .collect(Collectors.toList());

	    cnpjDTOResponse.setSocios(socioDTOs);

	    return cnpjDTOResponse;
	}

}
