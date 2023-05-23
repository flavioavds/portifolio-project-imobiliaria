package com.portifolio.imobiliaria.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "tb_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private UUID id;
	
	private String name;
	private String email;
	private String password;
	
	private boolean enabled;
	
	@Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_users_roles")
    @OrderColumn(name = "role_index")
    @Column(name = "role")
	@Builder.Default
	private Set<Role> roles = new HashSet<>();
	
	@JsonIgnore
	@Builder.Default
	private boolean deleted = Boolean.FALSE;
	
	@Lob
    private byte[] imagePerfil;
	
	public void updateProfileImage(byte[] imageBytes) {
        this.imagePerfil = imageBytes;
    }
	
	public byte[] getProfileImage() {
        return this.imagePerfil;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
	    return enabled;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
}
