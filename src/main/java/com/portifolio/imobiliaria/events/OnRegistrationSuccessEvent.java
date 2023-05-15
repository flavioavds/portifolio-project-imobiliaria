package com.portifolio.imobiliaria.events;

import java.io.Serial;
import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.portifolio.imobiliaria.dtos.person.CnpjDTOResponse;
import com.portifolio.imobiliaria.dtos.person.CpfDTOResponse;
import com.portifolio.imobiliaria.dtos.user.UserSignupDTOResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationSuccessEvent extends ApplicationEvent{

    @Serial
	private static final long serialVersionUID = 1L;
    private String appUrl;
	private Locale locale;
	private UserSignupDTOResponse user;
	private CpfDTOResponse cpf;
	private CnpjDTOResponse cnpj;

    public OnRegistrationSuccessEvent(UserSignupDTOResponse user, Locale locale, String appUrl) {
        super(user);
        this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
    }

	public OnRegistrationSuccessEvent(CpfDTOResponse cpf, Locale locale, String appUrl) {
		super(cpf);
		this.cpf = cpf;
		this.locale = locale;
		this.appUrl = appUrl;
	}
    
	public OnRegistrationSuccessEvent(CnpjDTOResponse cnpj, Locale locale, String appUrl) {
		super(cnpj);
		this.cnpj = cnpj;
		this.locale = locale;
		this.appUrl = appUrl;
	}
    
}