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

import brave.Tracer;
import feign.FeignException;

@Component
public class AuthenticationSuccesErrorHandler implements AuthenticationEventPublisher{
	
	private Logger logger = LoggerFactory.getLogger(AuthenticationSuccesErrorHandler.class);
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private Tracer tracer;
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();
//		if(authentication.getName().equalsIgnoreCase("fontEndAngularApp")){
//		    return; // si es igual a frontendapp se salen del método!
//		}
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
		StringBuilder builder = new StringBuilder();
		String msg = "Error en la autenticacion" + exception.getMessage();
		builder.append(msg);
		logger.error(msg);
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
			String intentosMsg = "intento "+usuario.getIntentos();
			builder.append(" - " + intentosMsg);
			logger.info(intentosMsg);
			tracer.currentSpan().tag("error.mensaje", builder.toString());
			usuarioService.update(usuario, usuario.getId());
		}catch (FeignException e) {
			logger.error("el usuario no existe "+ authentication.getName());
		}
		
	}
	
	
}
