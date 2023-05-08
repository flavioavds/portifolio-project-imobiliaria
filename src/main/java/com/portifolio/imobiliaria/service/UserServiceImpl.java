package com.portifolio.imobiliaria.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portifolio.imobiliaria.dtos.user.UpdateUserDTO;
import com.portifolio.imobiliaria.dtos.user.UserDTORequest;
import com.portifolio.imobiliaria.dtos.user.UserDTOResponse;
import com.portifolio.imobiliaria.dtos.user.UserMapper;
import com.portifolio.imobiliaria.dtos.user.UserSignupDTOResponse;
import com.portifolio.imobiliaria.entities.Role;
import com.portifolio.imobiliaria.entities.User;
import com.portifolio.imobiliaria.exception.UserNotFoundException;
import com.portifolio.imobiliaria.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MessageSource message;

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User updateUser(UUID id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        //existingUser.setUserRoles(user.getUserRoles());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

	@Override
	public UserSignupDTOResponse create(UserDTORequest dto, Locale locale) {
		if(userRepository.findByEmailIgnoreCase(dto.getEmail()).isPresent()) {
			throw new DataIntegrityViolationException(
					String.format(message.getMessage("user.message.error-already-registered", null, locale))
					);
		}
		return UserMapper.signupFromEntity(userRepository.save(userToEntity(dto, locale)));
	}
	
	private User userToEntity(UserDTORequest dto, Locale locale) {
	    User user = UserMapper.fromDTO(dto);
	    user.setEnabled(false);
	    
	    String userType = getUserType(dto);
	    user.setRoles(getUserRoles(userType));
	    
	    return user;
	}

	private String getUserType(UserDTORequest dto) {
	    String userType = "USER";
	    if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
	        userType = dto.getRoles().get(0);
	        if (!Arrays.asList("ADMIN", "USER").contains(userType.toUpperCase())) {
	            throw new RuntimeException("Tipo de usuário inválido");
	        }
	    }
	    return userType.toUpperCase();
	}

	private Set<Role> getUserRoles(String userType) {
	    if ("ADMIN".equals(userType)) {
	        return Collections.singleton(Role.ADMIN);
	    } else {
	        return Collections.singleton(Role.USER);
	    }
	}

	@Override
	public List<UserDTOResponse> findAll() {
		List<User> users = userRepository.findAll();
		return users.stream().map(UserMapper::fromEntity)
                .collect(Collectors.toList());
	}

	@Override
	public UserDTOResponse findById(UUID id, Locale locale) {
		return UserMapper.fromEntity(userVerify(id, locale));
	}
	
	private User userVerify(UUID id, Locale locale) {
		Optional<User> optional = userRepository.findById(id);
		if(optional.isEmpty())
			throw new EntityNotFoundException(
					String.format(message.getMessage("user.message.error-non-existent-entity-by-id", null, locale), id)
					);
		return optional.get();
	}

	@Override
	public UserDTOResponse update(UUID id, UpdateUserDTO dto, Locale locale) {
		User userOrigin = this.findByIdAuth(id, locale);
		BeanUtils.copyProperties(dto, userOrigin);
		
		return UserMapper.fromEntity(userRepository.save(userOrigin));
	}

	@Override
	public User findByIdAuth(UUID id, Locale locale) {
		return userVerify(id, locale);
	}

	@Override
	public void inactivate(UUID id, Locale locale) {
		User user = findByIdAuth(id, locale);
		user.setEnabled(false);
		userRepository.save(user);
		userRepository.deleteById(id);		
	}

	@Override
	public void activate(UUID id, Locale locale) {
		User user = findByIdAuth(id, locale);
		user.setDeleted(false);
		user.setEnabled(true);
		User updateUser = updateUserStepRole(user, locale);
		userRepository.save(updateUser);
		
	}
	
	private User updateUserStepRole(User userOrigin, Locale locale) {
	    if (userOrigin.getRoles() != null && !userOrigin.getRoles().isEmpty()) {
	        Set<Role> roles = userOrigin.getRoles();
	        if (roles.size() > 0) {
	            String userType = roles.iterator().next().name();
	            Set<Role> newRoles;
	            if (userType.equalsIgnoreCase("ADMIN")) {
	                newRoles = new HashSet<>(List.of(Role.ADMIN));
	            } else if (userType.equalsIgnoreCase("USER")) {
	                newRoles = new HashSet<>(List.of(Role.USER));
	            } else {
	                throw new RuntimeException("Tipo de usuário inválido");
	            }
	            userOrigin.setRoles(newRoles);
	        } else {
	            throw new RuntimeException("Lista de papéis vazia");
	        }
	    } else {
	        throw new RuntimeException("Tipo de usuário não especificado");
	    }
	    return userOrigin;
	}

}

