package com.microservicios.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservicios.usuariocommons.entities.Usuario;

@FeignClient("servicio-usuarios")
public interface UsuarioFeignClient {
	
	@GetMapping("usuarios/search/username")
	public Usuario findByUsername(@RequestParam("usuario") String usuario);
}
