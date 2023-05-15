package com.portifolio.imobiliaria.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portifolio.imobiliaria.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID>{

}
