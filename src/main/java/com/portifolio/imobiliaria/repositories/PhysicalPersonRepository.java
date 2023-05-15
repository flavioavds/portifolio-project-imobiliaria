package com.portifolio.imobiliaria.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portifolio.imobiliaria.entities.PhysicalPerson;

public interface PhysicalPersonRepository extends JpaRepository<PhysicalPerson, UUID>{
	
	Optional<PhysicalPerson> findById(UUID id);

}
