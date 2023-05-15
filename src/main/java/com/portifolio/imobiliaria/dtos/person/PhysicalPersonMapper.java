package com.portifolio.imobiliaria.dtos.person;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.entities.PhysicalPerson;

public class PhysicalPersonMapper {
	
	
	public static PhysicalPerson fromPersonDTOCpf(CpfDTORequest dto) {
		PhysicalPerson person = new PhysicalPerson();
		BeanUtils.copyProperties(dto, person);
		return person;
	}
	
	public static CpfDTOResponse cpfFromPhysicalPersonEntity(PhysicalPerson person) {
		CpfDTOResponse cpfDTOResponse = new CpfDTOResponse(
											person.getId(), 
											person.getName(), 
											person.getCpf());
		return cpfDTOResponse;
	}
}
