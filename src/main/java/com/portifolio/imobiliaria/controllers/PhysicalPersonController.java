package com.portifolio.imobiliaria.controllers;

import java.util.Locale;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portifolio.imobiliaria.dtos.person.CpfDTORequest;
import com.portifolio.imobiliaria.dtos.person.CpfDTOResponse;
import com.portifolio.imobiliaria.events.OnRegistrationSuccessEvent;
import com.portifolio.imobiliaria.service.PhysicalPersonService;

import jakarta.servlet.http.HttpServletRequest;

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

}
