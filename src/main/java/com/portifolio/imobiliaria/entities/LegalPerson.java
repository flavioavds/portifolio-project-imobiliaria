package com.portifolio.imobiliaria.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_legal_person")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalPerson {
	
	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "legalPerson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Socio> socios = new ArrayList<>();

    public void addSocio(Socio socio) {
        socios.add(socio);
        socio.setLegalPerson(this);
    }

    public void removeSocio(Socio socio) {
        socios.remove(socio);
        socio.setLegalPerson(null);
    }

}
