package com.portifolio.imobiliaria.security.config.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portifolio.imobiliaria.dtos.user.UserDTORequest;
import com.portifolio.imobiliaria.entities.Role;
import com.portifolio.imobiliaria.entities.Token;
import com.portifolio.imobiliaria.entities.TokenType;
import com.portifolio.imobiliaria.entities.User;
import com.portifolio.imobiliaria.repositories.TokenRepository;
import com.portifolio.imobiliaria.repositories.UserRepository;
import com.portifolio.imobiliaria.security.auth.AuthenticationRequest;
import com.portifolio.imobiliaria.security.auth.AuthenticationResponse;
import com.portifolio.imobiliaria.security.jwt.JwtService;
import com.portifolio.imobiliaria.security.jwt.RegisterRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
  private final MessageSource message;
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  
  public AuthenticationResponse register(UserDTORequest dto, Locale locale) {
      if (repository.findByEmailIgnoreCase(dto.getEmail()).isPresent()) {
          throw new DataIntegrityViolationException(
                  String.format(message.getMessage("user.message.error-already-registered", null, locale))
          );
      }

      User user = User.builder()
              .name(dto.getName())
              .email(dto.getEmail())
              .password(passwordEncoder.encode(dto.getPassword()))
              .roles(getUserRoles(dto.getRoles()))
              .enabled(true)
              .build();

      User savedUser = repository.save(user);
      String jwtToken = jwtService.generateToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);
      saveUserToken(savedUser, jwtToken);

      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
  }
  
  private UserDTORequest convertToUserDTORequest(RegisterRequest registerRequest) {
      return UserDTORequest.builder()
              .name(registerRequest.getName())
              .email(registerRequest.getEmail())
              .password(registerRequest.getPassword())
              .roles(registerRequest.getRoles())
              .userType(registerRequest.getUserType())
              .build();
  }

  public AuthenticationResponse register(RegisterRequest registerRequest, Locale locale) {
      UserDTORequest userDTORequest = convertToUserDTORequest(registerRequest);
      return register(userDTORequest, locale);
  }

  private Set<Role> getUserRoles(List<String> roles) {
      if (roles != null && !roles.isEmpty()) {
          return roles.stream()
                  .map(Role::valueOf)
                  .collect(Collectors.toSet());
      } else {
          return Collections.singleton(Role.USER);
      }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
    	.user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
		    HttpServletRequest request,
		    HttpServletResponse response
		) throws IOException {
		    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		        return;
		    }

		    final String refreshToken = authHeader.substring(7);
		    final String userEmail = jwtService.extractUsername(refreshToken);

		    if (userEmail != null) {
		        var user = this.repository.findByEmail(userEmail)
		                .orElseThrow();
		        if (jwtService.isTokenValid(refreshToken, user) && jwtService.isUserEnabled(user)) {
		            var accessToken = jwtService.generateToken(user);
		            revokeAllUserTokens(user);
		            saveUserToken(user, accessToken);
		            var authResponse = AuthenticationResponse.builder()
		                    .accessToken(accessToken)
		                    .refreshToken(refreshToken)
		                    .build();
		            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
		        }
		    }
		}

}
