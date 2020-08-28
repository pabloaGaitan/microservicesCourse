package com.microservicios.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.microservicios.oauth.clients.UsuarioFeignClient;
import com.microservicios.usuariocommons.entities.Usuario;

@Service
public class UsuarioService implements UserDetailsService{
	
	private Logger log = org.slf4j.LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private UsuarioFeignClient usuarioFeignClient;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioFeignClient.findByUsername(username);
		
		if(usuario == null) {
			log.error("No existe el usuario '"+username+"'");
			throw new UsernameNotFoundException("No existe el usuario '"+username+"'");
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(r -> new SimpleGrantedAuthority(r.getNombre()))
				.peek(a -> log.info(a.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("Usuario autenticado "+ username);
		
		return new User(username, usuario.getPassword(), usuario.getEnabled(), true, 
				true, true, authorities);
	}
	
	
}
