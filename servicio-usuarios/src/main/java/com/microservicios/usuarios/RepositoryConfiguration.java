package com.microservicios.usuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import com.microservicios.usuariocommons.entities.Rol;
import com.microservicios.usuariocommons.entities.Usuario;


@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer{

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Usuario.class, Rol.class);
	}
	

}
