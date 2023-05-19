package com.portifolio.imobiliaria.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTORequest;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoDTOResponse;
import com.portifolio.imobiliaria.dtos.endereco.EnderecoMapper;
import com.portifolio.imobiliaria.entities.Endereco;
import com.portifolio.imobiliaria.repositories.EnderecoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnderecoServiceImpl implements EnderecoService{
	
	private final EnderecoRepository repository;
	private final MessageSource messages;
	
	public EnderecoServiceImpl(EnderecoRepository repository, MessageSource messages) {
		this.repository = repository;
		this.messages = messages;
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

	        preencherCamposVazios(dto, endAux);

	        Endereco endereco = enderecoToEntity(dto, locale);
	        Endereco savedEndereco = repository.save(endereco);
	        return EnderecoMapper.fromEntity(savedEndereco);

	    } catch (IOException e) {
	        throw new RuntimeException("Erro ao salvar endereço", e);
	    } catch (JsonSyntaxException e) {
	        throw new RuntimeException("Resposta da API ViaCEP inválida", e);
	    }
	}

	private void preencherCamposVazios(EnderecoDTORequest dto, Endereco endAux) {
	    dto.setLogradouro(StringUtils.hasText(dto.getLogradouro()) ? dto.getLogradouro() : endAux.getLogradouro());
	    dto.setComplemento(StringUtils.hasText(dto.getComplemento()) ? dto.getComplemento() : endAux.getComplemento());
	    dto.setBairro(StringUtils.hasText(dto.getBairro()) ? dto.getBairro() : endAux.getBairro());
	    dto.setLocalidade(StringUtils.hasText(dto.getLocalidade()) ? dto.getLocalidade() : endAux.getLocalidade());
	    dto.setUf(StringUtils.hasText(dto.getUf()) ? dto.getUf() : endAux.getUf());
	}


	private Endereco enderecoToEntity(EnderecoDTORequest dto, Locale locale) {
	    return EnderecoMapper.fromDTO(dto);
	}

	@Override
	public Page<EnderecoDTOResponse> findAll(Pageable pageable) {
		Page<Endereco> enderecoPage = repository.findAll(pageable);
		
		return enderecoPage.map(endereco -> 
								{EnderecoDTOResponse enderecoDTOResponse = EnderecoMapper.fromEntity(endereco);
								return enderecoDTOResponse;
								});
	}

	@Override
	public EnderecoDTOResponse findById(UUID id, Locale locale) {
		Endereco endereco = findByEntityId(id, locale);
		EnderecoDTOResponse enderecoDTOResponse = EnderecoMapper.fromEntity(endereco);
		return enderecoDTOResponse;
	}
	
	private Endereco findByEntityId(UUID id, Locale locale) {
		Optional<Endereco> optional = repository.findById(id);
		if(optional.isEmpty()) {
			throw new EntityNotFoundException(
					String.format(messages.getMessage("endereco.message.error-non-existent-entity-by-id", null, locale), id)
					);
		}
		return optional.get();
	}

	@Override
	public EnderecoDTOResponse update(UUID id, EnderecoDTORequest dto, Locale locale) {
	    Endereco endereco = findByEntityId(id, locale);
	    
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

	        preencherCamposVazios(dto, endAux);

	        endereco.setCep(dto.getCep());
	        endereco.setLogradouro(dto.getLogradouro());
	        endereco.setComplemento(dto.getComplemento());
	        endereco.setNumero(dto.getNumero());
	        endereco.setBairro(dto.getBairro());
	        endereco.setLocalidade(dto.getLocalidade());
	        endereco.setUf(dto.getUf());

	        Endereco updatedEndereco = repository.save(endereco);
	        return EnderecoMapper.fromEntity(updatedEndereco);

	    } catch (IOException e) {
	        throw new RuntimeException("Erro ao atualizar endereço", e);
	    } catch (JsonSyntaxException e) {
	        throw new RuntimeException("Resposta da API ViaCEP inválida", e);
	    }
	}


	@Override
	public void deleteEndereco(UUID id, Locale locale) {
		Endereco endereco = findByEntityId(id, locale);
		repository.delete(endereco);
		
	}
	
	@Override
	public EnderecoDTOResponse findByCep(String cep, Locale locale) {
	    StringBuilder cepSemHifenBuilder = new StringBuilder();

	    for (char c : cep.toCharArray()) {
	        if (Character.isDigit(c)) {
	            cepSemHifenBuilder.append(c);
	        }
	    }

	    String cepSemHifen = cepSemHifenBuilder.toString();

	    // Adicionar o hífen no CEP caso tenha 8 dígitos
	    if (cepSemHifen.length() == 8) {
	        cepSemHifen = cepSemHifen.substring(0, 5) + "-" + cepSemHifen.substring(5);
	    }

	    Optional<Endereco> optional = repository.findByCep(cepSemHifen);
	    
	    if (optional.isEmpty()) {
	        throw new EntityNotFoundException(
	            String.format(messages.getMessage("endereco.message.error-not-found-cep", null, locale), cep)
	        );
	    }
	    
	    Endereco endereco = optional.get();
	    EnderecoDTOResponse enderecoDTOResponse = EnderecoMapper.fromEntity(endereco);
	    return enderecoDTOResponse;
	}

	@Override
	public Page<EnderecoDTOResponse> findByLogradouroStartingWithIgnoreCase(String logradouro, Locale locale, Pageable pageable) {
	    Page<Endereco> enderecoPage = repository.findByLogradouroStartingWithIgnoreCase(logradouro, pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage
	            .stream()
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}


	@Override
	public Page<EnderecoDTOResponse> findByComplementoStartingWithIgnoreCase(String complemento, Locale locale,
			Pageable pageable) {
		 Page<Endereco> enderecoPage = repository.findByComplementoStartingWithIgnoreCase(complemento, pageable);

		    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage
		            .stream()
		            .map(this::mapEnderecoDTOResponse)
		            .collect(Collectors.toList());

		    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}

	@Override
	public Page<EnderecoDTOResponse> findByNumeroStartingWithIgnoreCase(String numero, Locale locale,
			Pageable pageable) {
		Page<Endereco> enderecoPage = repository.findByNumeroStartingWithIgnoreCase(numero, pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage
	            .stream()
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}
	
	@Override
	public Page<EnderecoDTOResponse> findByNumeroBetween(String numero, Integer numeroInicial, Integer numeroFinal, Locale locale, Pageable pageable) {
	    Page<Endereco> enderecoPage = repository.findAll(pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage.getContent()
	            .stream()
	            .filter(endereco -> {
	                Integer enderecoNumero = extractNumericValues(endereco.getNumero());
	                return enderecoNumero >= numeroInicial && enderecoNumero <= numeroFinal;
	            })
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}

	
	public Page<Integer> findByNumeroExtract(String numero, Pageable pageable) {
	    Page<Endereco> enderecoPage = repository.findByNumero(numero, pageable);

	    List<Integer> numerosExtraidos = enderecoPage.getContent()
	            .stream()
	            .map(endereco -> extractNumericValues(endereco.getNumero()))
	            .collect(Collectors.toList());

	    return new PageImpl<>(numerosExtraidos, pageable, enderecoPage.getTotalElements());
	}

	private Integer extractNumericValues(String value) {
	    String numericValue = value.replaceAll("\\D+", "");
	    return Integer.parseInt(numericValue);
	}
	
	@Override
	public Page<EnderecoDTOResponse> findByNumeroLessThanOrEqual(String numero, Locale locale, Pageable pageable) {
	    Page<Endereco> enderecoPage = repository.findAll(pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage.getContent()
	            .stream()
	            .filter(endereco -> {
	                Integer enderecoNumero = extractNumericValues(endereco.getNumero());
	                return enderecoNumero <= extractNumericValues(numero);
	            })
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}

	@Override
	public Page<EnderecoDTOResponse> findByNumeroGreaterThanOrEqual(String numero, Locale locale, Pageable pageable) {
	    Page<Endereco> enderecoPage = repository.findAll(pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage.getContent()
	            .stream()
	            .filter(endereco -> {
	                Integer enderecoNumero = extractNumericValues(endereco.getNumero());
	                return enderecoNumero >= extractNumericValues(numero);
	            })
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}

	@Override
	public Page<EnderecoDTOResponse> findByBairroStartingWithIgnoreCase(String bairro, Locale locale,
			Pageable pageable) {
		Page<Endereco> enderecoPage = repository.findByBairroStartingWithIgnoreCase(bairro, pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage
	            .stream()
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}

	@Override
	public Page<EnderecoDTOResponse> findByLocalidadeStartingWithIgnoreCase(String localidade, Locale locale,
			Pageable pageable) {
		Page<Endereco> enderecoPage = repository.findByLocalidadeStartingWithIgnoreCase(localidade, pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage
	            .stream()
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}

	@Override
	public Page<EnderecoDTOResponse> findByUfStartingWithIgnoreCase(String uf, Locale locale, Pageable pageable) {
		Page<Endereco> enderecoPage = repository.findByUfStartingWithIgnoreCase(uf, pageable);

	    List<EnderecoDTOResponse> enderecoDTOResponses = enderecoPage
	            .stream()
	            .map(this::mapEnderecoDTOResponse)
	            .collect(Collectors.toList());

	    return new PageImpl<>(enderecoDTOResponses, pageable, enderecoPage.getTotalElements());
	}

	@Override
	public Page<EnderecoDTOResponse> findByLocalidadeAndBairroStartingWithIgnoreCase(String localidade, String bairro,
	        Locale locale, Pageable pageable) {
	    Page<Endereco> enderecoPage = repository.findByLocalidadeAndBairroStartingWithIgnoreCase(localidade, bairro, pageable);
	    return enderecoPage.map(this::mapEnderecoDTOResponse);
	}


	@Override
	public Page<EnderecoDTOResponse> findByLogradouroAndBairroStartingWithIgnoreCase(String logradouro, String bairro,
	        Locale locale, Pageable pageable) {
	    Page<Endereco> enderecoPage = repository.findByLogradouroAndBairroStartingWithIgnoreCase(logradouro, bairro, pageable);
	    return enderecoPage.map(this::mapEnderecoDTOResponse);
	}
	
	private EnderecoDTOResponse mapEnderecoDTOResponse(Endereco endereco) {
		EnderecoDTOResponse enderecoDTOResponse = new EnderecoDTOResponse();
		enderecoDTOResponse.setId(endereco.getId());
		enderecoDTOResponse.setCep(endereco.getCep());
		enderecoDTOResponse.setLogradouro(endereco.getLogradouro());
		enderecoDTOResponse.setComplemento(endereco.getComplemento());
		enderecoDTOResponse.setNumero(endereco.getNumero());
		enderecoDTOResponse.setBairro(endereco.getBairro());
		enderecoDTOResponse.setLocalidade(endereco.getLocalidade());
		enderecoDTOResponse.setUf(endereco.getUf());
		return enderecoDTOResponse;
	}
	
}

