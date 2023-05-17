package com.portifolio.imobiliaria.controllers;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portifolio.imobiliaria.dtos.person.CpfDTORequest;
import com.portifolio.imobiliaria.dtos.person.CpfDTOResponse;
import com.portifolio.imobiliaria.events.OnRegistrationSuccessEvent;
import com.portifolio.imobiliaria.service.PhysicalPersonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/physical-persons")
public class PhysicalPersonController {
	
	private final PhysicalPersonService service;
	private final ApplicationEventPublisher eventPublisher;
	
	public PhysicalPersonController(PhysicalPersonService service, ApplicationEventPublisher eventPublisher) {
		super();
		this.service = service;
		this.eventPublisher = eventPublisher;
	}
	
	@PostMapping("/cpf")
    public ResponseEntity<CpfDTOResponse> saveCpf(@RequestBody CpfDTORequest dto, 
    		@RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale,
    		HttpServletRequest request) {
    	
        CpfDTOResponse response = service.saveCpf(dto, new Locale(locale));
        eventPublisher.publishEvent(new OnRegistrationSuccessEvent(response,  new Locale(locale), request.getContextPath()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	@GetMapping("/all")
    public ResponseEntity<Page<CpfDTOResponse>> getAllPhysicalPersons(Pageable pageable) {
        Page<CpfDTOResponse> physicalPersonPage = service.findAll(pageable);
        return ResponseEntity.ok(physicalPersonPage);
    }
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CpfDTOResponse> getById(@PathVariable UUID id,
												   @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale){
		return ResponseEntity.ok(service.findById(id, new Locale(locale)));
	}
	
	@PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CpfDTOResponse> update(@PathVariable UUID id,
												  @RequestBody @Valid CpfDTORequest dto,
												  @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale){
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
					.body(service.update(id, dto, new Locale(locale)));
	}
	
	@DeleteMapping("/{physicalPersonId}")
    public ResponseEntity<String> deletePhysicalPerson(@PathVariable UUID physicalPersonId) {
        Locale locale = Locale.getDefault();

        service.deletePhysicalPerson(physicalPersonId, locale);

        String message = String.format("PhysicalPerson com ID %s foram excluídos com sucesso.", physicalPersonId);
        return ResponseEntity.ok(message);
    }
	
	@GetMapping("/cpf")
	public ResponseEntity<CpfDTOResponse> findByCpf(@RequestParam("cpf") String cpf, Locale locale) {
		CpfDTOResponse response = service.findByCpf(cpf, locale);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/search")
    public ResponseEntity<List<CpfDTOResponse>> findByNameStartingWithIgnoreCase(@RequestParam("name") String name,     		
					  															 @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
         List<CpfDTOResponse> legalPersons = service.findByNameStartingWithIgnoreCase(name, new Locale(locale));
        return ResponseEntity.ok(legalPersons);
    }

}
