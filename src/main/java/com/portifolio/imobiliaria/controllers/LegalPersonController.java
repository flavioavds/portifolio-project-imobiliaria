package com.portifolio.imobiliaria.controllers;

import java.util.Locale;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portifolio.imobiliaria.dtos.person.CnpjDTORequest;
import com.portifolio.imobiliaria.dtos.person.CnpjDTOResponse;
import com.portifolio.imobiliaria.events.OnRegistrationSuccessEvent;
import com.portifolio.imobiliaria.service.LegalPersonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/legal-persons")
public class LegalPersonController {
	
	private final LegalPersonService legalPersonService;
	private final ApplicationEventPublisher eventPublisher;
	
	public LegalPersonController(LegalPersonService legalPersonService, ApplicationEventPublisher eventPublisher) {
		super();
		this.legalPersonService = legalPersonService;
		this.eventPublisher = eventPublisher;
	}
	
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CnpjDTOResponse> create(@RequestBody @Valid CnpjDTORequest dto,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale,
                                                       HttpServletRequest request) {
		CnpjDTOResponse createdLegalPerson = legalPersonService.saveCnpj(dto, new Locale(locale));
		eventPublisher.publishEvent(new OnRegistrationSuccessEvent(createdLegalPerson,  new Locale(locale), request.getContextPath()));
		return ResponseEntity.status(HttpStatus.CREATED).body(createdLegalPerson);
    }

}
