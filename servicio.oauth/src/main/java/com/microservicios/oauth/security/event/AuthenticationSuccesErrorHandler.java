package com.microservicios.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.microservicios.oauth.services.IUsuarioService;
import com.microservicios.usuariocommons.entities.Usuario;

import feign.FeignException;

@Component
public class AuthenticationSuccesErrorHandler implements AuthenticationEventPublisher{
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationSuccesErrorHandler.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();
		logger.info("Success Login" + user.getUsername());
		try {
			Usuario usuario = usuarioService.findByUsername(user.getUsername());
			if(usuario.getIntentos()!=null && usuario.getIntentos()>=1) {
				usuario.setIntentos(0);
			}
			usuarioService.update(usuario, usuario.getId());
		}catch(FeignException e) {
			logger.error(String.format("El usuario %s no existe en el sistema", user.getUsername()));
		}
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		logger.error("Error en la autenticacion" + exception.getMessage());
		try { //capturar la excepcion de feign ya que para buscar el usuario hay que consultar un microservicio
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			if(usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}
			usuario.setIntentos(usuario.getIntentos()+1);
			
			if(usuario.getIntentos()>=3) {
				logger.info("usuario deshabilitado");
				usuario.setEnabled(false);
			}
			logger.info("intento"+usuario.getIntentos());
			usuarioService.update(usuario, usuario.getId());
		}catch (FeignException e) {
			logger.error("el usuario no existe "+ authentication.getName());
		}
		
	}
	
	
}
