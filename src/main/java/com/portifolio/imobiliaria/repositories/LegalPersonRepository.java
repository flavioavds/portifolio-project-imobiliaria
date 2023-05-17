package com.portifolio.imobiliaria.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portifolio.imobiliaria.entities.LegalPerson;

public interface LegalPersonRepository extends JpaRepository<LegalPerson, UUID>{

	Optional<LegalPerson> findById(UUID id);
	Optional<LegalPerson> findByNameIgnoreCase(String name);
	Optional<LegalPerson> findByCnpj(String cnpj);
	
	@Query("SELECT s FROM LegalPerson s WHERE lower(s.name) LIKE lower(concat(:name, '%'))")
	List<LegalPerson> findByNameStartingWithIgnoreCase(@Param("name") String name);
	
}
