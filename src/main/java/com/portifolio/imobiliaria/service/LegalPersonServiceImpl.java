package com.portifolio.imobiliaria.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.portifolio.imobiliaria.exception.DuplicatedCnpjException;
import com.portifolio.imobiliaria.exception.DuplicatedCpfException;
import com.portifolio.imobiliaria.exception.InvalidDocumentException;
import com.portifolio.imobiliaria.repositories.LegalPersonRepository;
import com.portifolio.imobiliaria.repositories.SocioRepository;
import com.portifolio.imobiliaria.valid.ValidationCPF_CNPJ;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LegalPersonServiceImpl implements LegalPersonService {
	
	private static final Logger logger = LoggerFactory.getLogger(LegalPersonServiceImpl.class);

	
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
	    String cnpj = dto.getCnpj().replaceAll("[^0-9]", ""); // Remove todos os caracteres não numéricos do CNPJ
	    cnpj = formatCnpj(cnpj); // Formata o CNPJ para o formato desejado

	    if (!validationCPF_CNPJ.isValidCNPJ(cnpj)) {
	        throw new InvalidDocumentException(
	            String.format(messages.getMessage("person.message.error-ivalid-cnpj", null, locale))
	        );
	    }

	    Optional<LegalPerson> existingLegalPerson = repository.findByCnpj(cnpj);
	    if (existingLegalPerson.isPresent()) {
	        throw new DuplicatedCnpjException(
	            String.format(messages.getMessage("person.message.error-cnpj-already-registered", null, locale))
	        );
	    }

	    legalPerson.setCnpj(cnpj); // Salva o CNPJ diretamente no formato desejado

	    List<Socio> socios = new ArrayList<>();

	    for (SocioDTORequest socioDTO : dto.getSocios()) {
	        String cpf = formatCpf(socioDTO.getCpf().replaceAll("[^0-9]", "")); // Remove todos os caracteres não numéricos do CPF e formata o CPF

	        if (!validationCPF_CNPJ.isValidCPF(cpf)) {
	            throw new InvalidDocumentException(
	                String.format(messages.getMessage("person.message.error-ivalid-cpf", null, locale))
	            );
	        }
	        Socio socio = SocioMapper.fromDTO(socioDTO);
	        socio.setLegalPerson(legalPerson);
	        socio.setCpf(cpf); // Salva o CPF diretamente no formato desejado
	        socios.add(socio);
	    }

	    legalPerson.setSocios(socios);

	    LegalPerson savedLegalPerson = repository.save(legalPerson);

	    // Remover pontos e barras novamente antes de salvar no banco
	    String formattedCnpj = savedLegalPerson.getCnpj().replaceAll("[^0-9]", "");
	    savedLegalPerson.setCnpj(formatCnpj(formattedCnpj));

	    CnpjDTOResponse cnpjDTOResponse = LegalPersonMapper.fromEntity(savedLegalPerson);
	    cnpjDTOResponse.setSocios(socios.stream()
	            .map(SocioMapper::toDTO)
	            .collect(Collectors.toList()));

	    return cnpjDTOResponse;
	}

	private String formatCnpj(String cnpj) {
	    if (cnpj.length() == 14) {
	        return cnpj;
	    } else if (cnpj.length() == 18) {
	        return cnpj.replaceAll("[^0-9]", "");
	    } else {
	        throw new IllegalArgumentException("CNPJ inválido: " + cnpj);
	    }
	}

	private String formatCpf(String cpf) {
	    return cpf.replaceAll("[^0-9]", "");
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
	    String cnpj = formatCnpj(dto.getCnpj()); // Formata o CNPJ para o formato desejado

	    if (!validationCPF_CNPJ.isValidCNPJ(cnpj)) {
	        throw new InvalidDocumentException(
	            String.format(messages.getMessage("person.message.error-ivalid-cnpj", null, locale))
	        );
	    }

	    Optional<LegalPerson> existingLegalPerson = repository.findByCnpj(cnpj);
	    if (existingLegalPerson.isPresent() && !existingLegalPerson.get().getId().equals(id)) {
	        throw new DuplicatedCnpjException(
	            String.format(messages.getMessage("person.message.error-cnpj-already-registered", null, locale))
	        );
	    }

	    legalPerson.setCnpj(cnpj); // Salva o CNPJ diretamente no formato desejado

	    List<Socio> updatedSocios = new ArrayList<>();

	    for (SocioDTORequest socioDTO : dto.getSocios()) {
	        String cpf = formatCpf(socioDTO.getCpf()); // Formata o CPF para o formato desejado

	        if (!validationCPF_CNPJ.isValidCPF(cpf)) {
	            throw new InvalidDocumentException(
	                String.format(messages.getMessage("person.message.error-ivalid-cpf", null, locale))
	            );
	        }

	        Optional<Socio> existingSocio = existingLegalPerson.flatMap(person -> person.getSocios().stream()
	                .filter(socio -> formatCpf(socio.getCpf()).equals(cpf))
	                .findFirst());

	        if (existingSocio.isPresent() && !existingSocio.get().getLegalPerson().getId().equals(id)) {
	            throw new DuplicatedCpfException(
	                String.format(messages.getMessage("person.message.error-cpf-already-registered", null, locale))
	            );
	        }
	        
	        Socio socio = existingSocio.orElseGet(() -> SocioMapper.fromDTO(socioDTO));
	        socio.setLegalPerson(legalPerson);
	        socio.setName(socioDTO.getName());
	        socio.setCpf(formatCpf(socioDTO.getCpf())); // Formata o CPF antes de salvar
	        updatedSocios.add(socio);

	    }

	    // Define a lista atualizada de sócios
	    legalPerson.getSocios().clear();
	    legalPerson.getSocios().addAll(updatedSocios);

	    LegalPerson savedLegalPerson = repository.save(legalPerson);

	    // Remover pontos e traço novamente antes de salvar no banco
	    savedLegalPerson.getSocios().forEach(socio -> socio.setCpf(formatCpf(socio.getCpf())));

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
	public CnpjDTOResponse addSocio(UUID legalPersonId, SocioDTORequest socioDTO, Locale locale) {
	    logger.info("Adding socio: legalPersonId={}, socioDTO={}", legalPersonId, socioDTO);

	    LegalPerson legalPerson = legalPersonVerifyById(legalPersonId, locale);
	    logger.info("LegalPerson retrieved: {}", legalPerson);

	    String cpf = formatCpf(socioDTO.getCpf());

	    ValidationCPF_CNPJ validationCPF_CNPJ = new ValidationCPF_CNPJ();
	    if (!validationCPF_CNPJ.isValidCPF(cpf)) {
	        throw new InvalidDocumentException(
	            String.format(messages.getMessage("person.message.error-invalid-cpf", null, locale))
	        );
	    }

	    Optional<Socio> existingSocio = legalPerson.getSocios().stream()
	            .filter(socio -> formatCpf(socio.getCpf()).equals(cpf))
	            .findFirst();

	    if (existingSocio.isPresent()) {
	        throw new DuplicatedCpfException(
	            String.format(messages.getMessage("person.message.error-cpf-already-registered", null, locale))
	        );
	    }

	    Socio socio = SocioMapper.fromDTO(socioDTO);
	    socio.setLegalPerson(legalPerson);
	    socio.setName(socioDTO.getName());

	    legalPerson.getSocios().add(socio);
	    repository.save(legalPerson);

	    // Remover pontos e traço novamente antes de retornar a resposta
	    legalPerson.getSocios().forEach(s -> s.setCpf(formatCpf(s.getCpf())));

	    CnpjDTOResponse cnpjDTOResponse = LegalPersonMapper.fromEntity(legalPerson);
	    return cnpjDTOResponse;
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
