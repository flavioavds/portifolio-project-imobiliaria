package com.portifolio.imobiliaria.controllers;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTORequest;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTOResponse;
import com.portifolio.imobiliaria.service.EnderecoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/endereco")
public class EnderecoController {
	
	@Autowired
	private EnderecoService service;
	
	 @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<EnderecoDTOResponse> cadastrar(@RequestBody EnderecoDTORequest endereco) {
	        EnderecoDTOResponse enderecoSalvo = service.saveEndereco(endereco, Locale.getDefault());
			return ResponseEntity.ok().body(enderecoSalvo);
	    }
	 
	 @GetMapping("/all")
	    public ResponseEntity<Page<EnderecoDTOResponse>> getAllEndereco(Pageable pageable) {
	        Page<EnderecoDTOResponse> enderecoPage = service.findAll(pageable);
	        return ResponseEntity.ok(enderecoPage);
	    }
	 
	 @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<EnderecoDTOResponse> getById(@PathVariable UUID id,
													   @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale){
			return ResponseEntity.ok(service.findById(id, new Locale(locale)));
		}
	 
	 @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<EnderecoDTOResponse> update(@PathVariable UUID id,
													  @RequestBody @Valid EnderecoDTORequest dto,
													  @RequestHeader(value = "Accept-Language", defaultValue = "en", required = false) String locale){
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body(service.update(id, dto, new Locale(locale)));
		}
	 
	 @DeleteMapping("/{enderecoId}")
	    public ResponseEntity<String> deletePhysicalPerson(@PathVariable UUID enderecoId) {
	        Locale locale = Locale.getDefault();

	        service.deleteEndereco(enderecoId, locale);

	        String message = String.format("Endereço com ID %s foram excluídos com sucesso.", enderecoId);
	        return ResponseEntity.ok(message);
	    }
	 
	 @GetMapping("/cep")
	 public ResponseEntity<EnderecoDTOResponse> findByCep(@RequestParam("cep") String cep, Locale locale) {
	     EnderecoDTOResponse response = service.findByCep(cep, locale);
	     return ResponseEntity.ok(response);
	 }


}
