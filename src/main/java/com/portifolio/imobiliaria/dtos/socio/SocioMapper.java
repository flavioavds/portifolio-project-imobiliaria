package com.portifolio.imobiliaria.dtos.socio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.entities.Socio;

public class SocioMapper {
	
	public static List<Socio> fromDTOSocio(List<SocioDTORequest> list) {
	    List<Socio> socios = new ArrayList<>();
	    for (SocioDTORequest socioDTORequest : list) {
	        Socio socio = new Socio();
	        BeanUtils.copyProperties(socioDTORequest, socio);
	        socios.add(socio);
	    }
	    return socios;
	}

	
	public static SocioDTOResponse socioFromEntity(Socio socio) {
	    SocioDTOResponse socioDTOResponse = new SocioDTOResponse(
	        socio.getId(),
	        socio.getName(),
	        socio.getCpf()
	    );
	    return socioDTOResponse;
	}

	public static Socio fromDTO(SocioDTORequest dto) {
        Socio socio = new Socio();
        BeanUtils.copyProperties(dto, socio);
        return socio;
    }

}
