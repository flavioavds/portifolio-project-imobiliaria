package com.portifolio.imobiliaria.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portifolio.imobiliaria.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	Optional<User> findByEmailIgnoreCase(String email);
	Optional<User> findById(UUID id);
	Optional<User> findByNameIgnoreCase(String name);

}
