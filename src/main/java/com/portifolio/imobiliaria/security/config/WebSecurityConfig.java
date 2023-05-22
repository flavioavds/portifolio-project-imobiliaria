package com.portifolio.imobiliaria.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.portifolio.imobiliaria.security.jwt.AuthEntryPointJwt;
import com.portifolio.imobiliaria.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	private static final String[] AUTH_WHITELIST = {
			
			"/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui.html", "/webjars/**",
			"/v3/api-docs/**", "/swagger-ui/**", "/api-docs/**",
			"/swagger-ui.html","swagger-ui/**"
	};
	
	private final AuthEntryPointJwt unauthorizeHandler;	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;
	
	public WebSecurityConfig(AuthEntryPointJwt unauthorizeHandler, JwtAuthenticationFilter jwtAuthFilter,
			AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler) {
		super();
		this.unauthorizeHandler = unauthorizeHandler;
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationProvider = authenticationProvider;
		this.logoutHandler = logoutHandler;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll())
	        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
	        .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")));
	    http
	    .csrf(csrf -> csrf.disable())
	    .authorizeHttpRequests(auth -> auth
	        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
	        .requestMatchers("/v1/auth/**").permitAll()
	        .requestMatchers(AUTH_WHITELIST).permitAll()
	        .anyRequest().authenticated()
	    		);
	    
	    http
        .logout(logout -> logout
            .logoutUrl("/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        )
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizeHandler));

	    return http.build();
	}




}
