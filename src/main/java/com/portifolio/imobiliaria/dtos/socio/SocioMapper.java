package com.portifolio.imobiliaria.dtos.socio;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.entities.Socio;

public class SocioMapper {

    public static Socio fromDTO(SocioDTORequest dto) {
        Socio socio = new Socio();
        BeanUtils.copyProperties(dto, socio);
        return socio;
    }

    public static SocioDTOResponse toDTO(Socio socio) {
        SocioDTOResponse socioDTOResponse = new SocioDTOResponse();
        BeanUtils.copyProperties(socio, socioDTOResponse);
        return socioDTOResponse;
    }

    public static Socio fromEntity(SocioDTOResponse entity) {
        Socio socio = new Socio();
        BeanUtils.copyProperties(entity, socio);
        return socio;
    }

    public static SocioDTOResponse fromEntity(Socio entity) {
        SocioDTOResponse socioDTOResponse = new SocioDTOResponse();
        BeanUtils.copyProperties(entity, socioDTOResponse);
        return socioDTOResponse;
    }
}