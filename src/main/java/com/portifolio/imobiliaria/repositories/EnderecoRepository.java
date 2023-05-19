package com.portifolio.imobiliaria.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portifolio.imobiliaria.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID>{
	
	Optional<Endereco> findById(UUID id);
	Optional<Endereco> findByCep(String cep);
	Page<Endereco> findByNumero(String numero, Pageable pageable);
	
	@Query("SELECT s FROM Endereco s WHERE lower(s.logradouro) LIKE lower(concat(:logradouro, '%'))")
	Page<Endereco> findByLogradouroStartingWithIgnoreCase(@Param("logradouro") String logradouro, Pageable pageable);
	
	@Query("SELECT s FROM Endereco s WHERE lower(s.complemento) LIKE lower(concat(:complemento, '%'))")
	Page<Endereco> findByComplementoStartingWithIgnoreCase(@Param("complemento") String complemento, Pageable pageable);
	
	@Query("SELECT s FROM Endereco s WHERE lower(s.numero) LIKE lower(concat(:numero, '%'))")
	Page<Endereco> findByNumeroStartingWithIgnoreCase(@Param("numero") String numero, Pageable pageable);
	
	@Query("SELECT e FROM Endereco e WHERE e.numero >= :numeroInicial AND e.numero <= :numeroFinal")
	Page<Endereco> findByNumeroBetween(@Param("numeroInicial") String numeroInicial, @Param("numeroFinal") String numeroFinal, Pageable pageable);
	
	@Query("SELECT e FROM Endereco e WHERE e.numero <= :numero")
	List<Endereco> findByNumeroLessThanOrEqual(@Param("numero") String numero);
	
	@Query("SELECT e FROM Endereco e WHERE e.numero >= :numero")
	List<Endereco> findByNumeroGreaterThanOrEqual(@Param("numero") String numero);
	
	@Query("SELECT s FROM Endereco s WHERE lower(s.bairro) LIKE lower(concat(:bairro, '%'))")
	Page<Endereco> findByBairroStartingWithIgnoreCase(@Param("bairro") String bairro, Pageable pageable);
	
	@Query("SELECT s FROM Endereco s WHERE lower(s.localidade) LIKE lower(concat(:localidade, '%'))")
	Page<Endereco> findByLocalidadeStartingWithIgnoreCase(@Param("localidade") String localidade, Pageable pageable);
	
	@Query("SELECT s FROM Endereco s WHERE lower(s.uf) LIKE lower(concat(:uf, '%'))")
	Page<Endereco> findByUfStartingWithIgnoreCase(@Param("uf") String uf, Pageable pageable);
	
	@Query("SELECT s FROM Endereco s WHERE lower(s.localidade) LIKE lower(concat(:localidade, '%')) " +
	           "AND lower(s.bairro) LIKE lower(concat(:bairro, '%'))")
	    Page<Endereco> findByLocalidadeAndBairroStartingWithIgnoreCase(
	            @Param("localidade") String localidade, 
	            @Param("bairro") String bairro,
	            Pageable pageable);

	    @Query("SELECT s FROM Endereco s WHERE lower(s.logradouro) LIKE lower(concat(:logradouro, '%')) " +
	           "AND lower(s.bairro) LIKE lower(concat(:bairro, '%'))")
	    Page<Endereco> findByLogradouroAndBairroStartingWithIgnoreCase(
	            @Param("logradouro") String logradouro,
	            @Param("bairro") String bairro,
	            Pageable pageable);

}
