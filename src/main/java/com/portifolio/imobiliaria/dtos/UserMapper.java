package com.portifolio.imobiliaria.dtos;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.beans.BeanUtils;

import com.portifolio.imobiliaria.entities.Role;
import com.portifolio.imobiliaria.entities.User;

public class UserMapper {
	
	public static User fromDTO(UserDTORequest dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		user.setPassword(dto.getPassword());
		user.setRoles(new HashSet<>());
		
		return user;
	}
	
	public static UserDTOResponse fromEntity(User user) {
		UserDTOResponse userDTOResponse = new UserDTOResponse(
				user.getId(), 
				user.getName(),
				user.getEmail(), 
				user.getPassword(), 
				new ArrayList<>());
		
		if(user.getRoles() != null) {
	        user.getRoles().forEach(role -> {
	            if (role.equals(Role.ADMIN)) {
	            	userDTOResponse.getRoles().add(Role.ADMIN);
	            } else if (role.equals(Role.USER)) {
	            	userDTOResponse.getRoles().add(Role.USER);
	            }
	        });
	    }
		
		return userDTOResponse;
				
	}
	
	public static UserSignupDTOResponse signupFromEntity(User user) {
	    UserSignupDTOResponse userSignupDTOResponse = new UserSignupDTOResponse(
	            user.getId(), 
	            user.getName(), 
	            user.getEmail(), 
	            user.getPassword(), 
	            new ArrayList<>());
	    
	    if(user.getRoles() != null) {
	        user.getRoles().forEach(role -> {
	            if (role.equals(Role.ADMIN)) {
	                userSignupDTOResponse.getRoles().add(Role.ADMIN);
	            } else if (role.equals(Role.USER)) {
	                userSignupDTOResponse.getRoles().add(Role.USER);
	            }
	        });
	    }
	    return userSignupDTOResponse;
	}


}
