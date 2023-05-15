package com.portifolio.imobiliaria.dtos.person;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.entities.LegalPerson;

public class LegalPersonMapper {
	
	public static CnpjDTOResponse fromEntity(LegalPerson person) {
        CnpjDTOResponse cnpjDTOResponse = new CnpjDTOResponse();
        BeanUtils.copyProperties(person, cnpjDTOResponse);
        return cnpjDTOResponse;
    }

    public static LegalPerson fromDTO(CnpjDTORequest dto) {
        LegalPerson legalPerson = new LegalPerson();
        BeanUtils.copyProperties(dto, legalPerson);
        return legalPerson;
    }

}
