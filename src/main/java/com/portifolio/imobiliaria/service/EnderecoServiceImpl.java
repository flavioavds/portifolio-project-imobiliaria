package com.portifolio.imobiliaria.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTORequest;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTOResponse;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoMapper;
import com.portifolio.imobiliaria.entities.Endereco;
import com.portifolio.imobiliaria.repositories.EnderecoRepository;

@Service
public class EnderecoServiceImpl implements EnderecoService{
	
	private final EnderecoRepository repository;
	//private final MessageSource message;
	
	public EnderecoServiceImpl(EnderecoRepository repository, MessageSource message) {
		this.repository = repository;
		//this.message = message;
	}

	@Override
	public EnderecoDTOResponse saveEndereco(EnderecoDTORequest dto, Locale locale) {
	    String url = "https://viacep.com.br/ws/" + dto.getCep() + "/json/";
	    
	    try (InputStream is = new URL(url).openStream();
	            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
	        
	        StringBuilder jsonCep = new StringBuilder();
	        String cep;
	        while ((cep = br.readLine()) != null) {
	            jsonCep.append(cep);            
	        }
	        
	        Endereco endAux = new Gson().fromJson(jsonCep.toString(), Endereco.class);
	        dto.setCep(endAux.getCep());
	        dto.setLogradouro(endAux.getLogradouro());
	        dto.setComplemento(endAux.getComplemento());
	        dto.setBairro(endAux.getBairro());
	        dto.setLocalidade(endAux.getLocalidade());
	        dto.setUf(endAux.getUf());
	        
	        Endereco endereco = enderecoToEntity(dto, locale);
	        Endereco savedEndereco = repository.save(endereco);
	        return EnderecoMapper.fromEntity(savedEndereco);
	        
	    } catch (IOException e) {
	        throw new RuntimeException("Erro ao salvar endereço", e);
	    } catch (JsonSyntaxException e) {
	        throw new RuntimeException("Resposta da API ViaCEP inválida", e);
	    }
	}

	private Endereco enderecoToEntity(EnderecoDTORequest dto, Locale locale) {
	    return EnderecoMapper.fromDTO(dto);
	}

	
	

}
