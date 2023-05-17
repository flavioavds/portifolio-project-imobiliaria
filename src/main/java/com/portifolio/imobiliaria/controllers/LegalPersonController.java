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

import com.portifolio.imobiliaria.dtos.person.CnpjDTORequest;
import com.portifolio.imobiliaria.dtos.person.CnpjDTOResponse;
import com.portifolio.imobiliaria.dtos.socio.SocioDTOResponse;
import com.portifolio.imobiliaria.entities.Socio;
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
	
	@GetMapping("/all")
    public ResponseEntity<Page<CnpjDTOResponse>> getAllLegalPersons(Pageable pageable) {
        Page<CnpjDTOResponse> legalPersonPage = legalPersonService.findAll(pageable);
        return ResponseEntity.ok(legalPersonPage);
    }
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CnpjDTOResponse> getById(@PathVariable UUID id,
												   @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale){
		return ResponseEntity.ok(legalPersonService.findById(id, new Locale(locale)));
	}
	
	@PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CnpjDTOResponse> update(@PathVariable UUID id,
												  @RequestBody @Valid CnpjDTORequest dto,
												  @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale){
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
					.body(legalPersonService.update(id, dto, new Locale(locale)));
	}
	
	@DeleteMapping("/{legalPersonId}/socios/{socioId}")
    public ResponseEntity<String> deleteSocio(@PathVariable UUID legalPersonId, @PathVariable UUID socioId) {
        Locale locale = Locale.getDefault();

        legalPersonService.deleteSocio(legalPersonId, socioId, locale);

        String message = String.format("Sócio com ID %s excluído com sucesso do LegalPerson com ID %s.", socioId, legalPersonId);
        return ResponseEntity.ok(message);
    }
	
	@DeleteMapping("/{legalPersonId}")
    public ResponseEntity<String> deleteLegalPerson(@PathVariable UUID legalPersonId) {
        Locale locale = Locale.getDefault();

        legalPersonService.deleteLegalPerson(legalPersonId, locale);

        String message = String.format("LegalPerson com ID %s e todos os seus sócios foram excluídos com sucesso.", legalPersonId);
        return ResponseEntity.ok(message);
    }
	
	@GetMapping("/{id}/socios/{cpf}")
    public ResponseEntity<SocioDTOResponse> findSocioByCpf(@PathVariable("id") UUID legalPersonId, @PathVariable("cpf") String cpf) {
        SocioDTOResponse socio = legalPersonService.findSocioByCpf(legalPersonId, cpf, Locale.getDefault());
        return ResponseEntity.ok(socio);
    }
	
	@GetMapping("/socios/names")
	public ResponseEntity<List<Socio>> findSociosBySocioNameStartingWith(@RequestParam String name,
			 															 @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
	    List<Socio> socios = legalPersonService.findBySociosNameStartingWithIgnoreCase(name, new Locale(locale));
	    return ResponseEntity.ok(socios);
	}
	
	@GetMapping("/cnpj")
	public ResponseEntity<CnpjDTOResponse> findByCnpj(@RequestParam("cnpj") String cnpj, Locale locale) {
	    CnpjDTOResponse response = legalPersonService.findByCnpj(cnpj, locale);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/search")
    public ResponseEntity<List<CnpjDTOResponse>> findByNameStartingWithIgnoreCase(@RequestParam("name") String name,     		
					  															 @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale) {
         List<CnpjDTOResponse> legalPersons = legalPersonService.findByNameStartingWithIgnoreCase(name, new Locale(locale));
        return ResponseEntity.ok(legalPersons);
    }

}
