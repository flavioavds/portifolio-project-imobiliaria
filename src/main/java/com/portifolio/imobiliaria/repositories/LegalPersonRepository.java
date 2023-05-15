package com.portifolio.imobiliaria.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portifolio.imobiliaria.entities.LegalPerson;

public interface LegalPersonRepository extends JpaRepository<LegalPerson, UUID>{

	Optional<LegalPerson> findById(UUID id);
	
}
