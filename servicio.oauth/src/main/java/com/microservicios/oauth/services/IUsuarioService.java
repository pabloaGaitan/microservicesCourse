package com.microservicios.oauth.services;

import com.microservicios.usuariocommons.entities.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);

}
