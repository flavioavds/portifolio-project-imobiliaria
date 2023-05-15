package com.portifolio.imobiliaria.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTORequest;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTOResponse;
import com.portifolio.imobiliaria.service.EnderecoService;

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

}
