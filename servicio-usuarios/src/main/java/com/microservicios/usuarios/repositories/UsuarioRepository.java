package com.microservicios.usuarios.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.microservicios.usuarios.entities.Usuario;

@RepositoryRestResource(path = "usuarios")
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	@RestResource(path = "username")
	public Usuario findByUsername(@Param("usuario") String username);
}
