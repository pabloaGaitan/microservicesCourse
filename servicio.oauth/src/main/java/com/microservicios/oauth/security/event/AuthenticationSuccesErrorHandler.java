package com.microservicios.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccesErrorHandler implements AuthenticationEventPublisher{
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationSuccesErrorHandler.class);
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();
		logger.info("Success Login" + user.getUsername());
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		logger.error("Error en la autenticacion" + exception.getMessage());
	}
	
	
}
