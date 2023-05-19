package com.portifolio.imobiliaria.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portifolio.imobiliaria.entities.PhysicalPerson;

public interface PhysicalPersonRepository extends JpaRepository<PhysicalPerson, UUID>{
	
	Optional<PhysicalPerson> findById(UUID id);
	Optional<PhysicalPerson> findByNameIgnoreCase(String name);
	Optional<PhysicalPerson> findByCpf(String cnpj);
	
	@Query("SELECT s FROM PhysicalPerson s WHERE lower(s.name) LIKE lower(concat(:name, '%'))")
	List<PhysicalPerson> findByNameStartingWithIgnoreCase(@Param("name") String name);
	
	@Query("SELECT p FROM PhysicalPerson p WHERE p.cpf = :cpf AND p.id <> :id")
    Optional<PhysicalPerson> findByCpfExcludingId(@Param("cpf") String cpf, @Param("id") UUID id);


}
