package com.portifolio.imobiliaria.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portifolio.imobiliaria.entities.Socio;

public interface SocioRepository extends JpaRepository<Socio, UUID> {
	
	@Query("SELECT s FROM Socio s WHERE lower(s.name) LIKE lower(concat(:name, '%'))")
    List<Socio> findByNameStartingWithIgnoreCase(@Param("name") String name);

}
