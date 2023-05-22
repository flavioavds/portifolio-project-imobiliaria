package com.portifolio.imobiliaria.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_socio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Socio {

	@Id
	@GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    //@Column(unique = true)
    private String cpf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_person_id")
    @JsonIgnore // Adicionado para evitar recursão infinita
    private LegalPerson legalPerson;

	@Override
	public String toString() {
		return "Socio [id=" + id + ", name=" + name + ", cpf=" + cpf + ", legalPerson=" + legalPerson + "]";
	}

}
