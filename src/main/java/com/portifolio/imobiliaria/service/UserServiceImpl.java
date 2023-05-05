package com.portifolio.imobiliaria.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portifolio.imobiliaria.dtos.UserDTORequest;
import com.portifolio.imobiliaria.dtos.UserMapper;
import com.portifolio.imobiliaria.dtos.UserSignupDTOResponse;
import com.portifolio.imobiliaria.entities.Role;
import com.portifolio.imobiliaria.entities.User;
import com.portifolio.imobiliaria.exception.UserNotFoundException;
import com.portifolio.imobiliaria.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MessageSource message;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
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
		
		// Verifica o tipo de usuário e adiciona a role correspondente
		if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
			String userType = dto.getRoles().get(0);
			if (userType.equalsIgnoreCase("ADMIN")) {
				user.setRoles(new HashSet<>(List.of(Role.ADMIN)));
			} else if (userType.equalsIgnoreCase("USER")) {
				user.setRoles(new HashSet<>(List.of(Role.USER)));
			} else {
				throw new RuntimeException("Tipo de usuário inválido");
			}
		} else {
			throw new RuntimeException("Tipo de usuário não especificado");
		}
		
		return user;
	}

}

