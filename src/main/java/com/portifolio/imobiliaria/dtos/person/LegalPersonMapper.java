package com.portifolio.imobiliaria.dtos.person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.dtos.socio.SocioDTOResponse;
import com.portifolio.imobiliaria.dtos.socio.SocioMapper;
import com.portifolio.imobiliaria.entities.LegalPerson;
import com.portifolio.imobiliaria.entities.Socio;

public class LegalPersonMapper {
	
//	public static CnpjDTOResponse fromEntity(LegalPerson person) {
//        CnpjDTOResponse cnpjDTOResponse = new CnpjDTOResponse();
//        BeanUtils.copyProperties(person, cnpjDTOResponse);
//        return cnpjDTOResponse;
//    }
	
	public static CnpjDTOResponse fromEntity(LegalPerson legalPerson) {
	    CnpjDTOResponse cnpjDTOResponse = new CnpjDTOResponse();
	    BeanUtils.copyProperties(legalPerson, cnpjDTOResponse);

	    List<SocioDTOResponse> sociosDTO = new ArrayList<>();
	    if (legalPerson.getSocios() != null) {
	        for (Socio socio : legalPerson.getSocios()) {
	            SocioDTOResponse socioDTO = SocioMapper.toDTO(socio);
	            sociosDTO.add(socioDTO);
	        }
	    }

	    cnpjDTOResponse.setSocios(sociosDTO);
	    return cnpjDTOResponse;
	}


    public static LegalPerson fromDTO(CnpjDTORequest dto) {
        LegalPerson legalPerson = new LegalPerson();
        BeanUtils.copyProperties(dto, legalPerson);
        return legalPerson;
    }
    
    public static CnpjDTOResponse toDTO(LegalPerson person) {
        CnpjDTOResponse cnpjDTOResponse = new CnpjDTOResponse();
        BeanUtils.copyProperties(person, cnpjDTOResponse);

        List<SocioDTOResponse> sociosDTO = new ArrayList<>();
        if (person.getSocios() != null) {
            for (Socio socio : person.getSocios()) {
                SocioDTOResponse socioDTO = SocioMapper.toDTO(socio);
                sociosDTO.add(socioDTO);
            }
        }

        cnpjDTOResponse.setSocios(sociosDTO);
        return cnpjDTOResponse;
    }

}
