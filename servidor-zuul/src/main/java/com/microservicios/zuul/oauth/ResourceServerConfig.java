package com.microservicios.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Value("${config.security.oauth.client.jwt.key}")
	private String jwtSecret;
	//Configuracion del Token
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}
	
	//Configuracion de las rutas
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()//url de autenticacion
			.antMatchers(HttpMethod.GET, "/api/productos","/api/items","/api/usuarios/usuarios").permitAll()
			.antMatchers(HttpMethod.GET, "/api/productos/{id}", "/api/item/{id}/cantidad/{cantidad}", "/api/usuarios/usuarios/{id}")
				.hasAnyRole("ADMIN","USER")//no es necesario poner el ROL_
			.antMatchers(HttpMethod.POST, "/api/productos","api/items/crear","/api/usuarios/usuarios").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/api/productos/{id}","/api/items/{id}","/api/usuarios/usuarios/{id}").hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/api/productos/{id}","/api/items/{id}","/api/usuarios/usuarios/{id}").hasRole("ADMIN")
			.anyRequest().authenticated();
		//.antMatchers( "api/productos/**","api/items/**","api/usuarios/usuarios/**").hasRole("ADMIN") forma resumida, las rutas especificas y metodos deben ir primero
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtSecret);
		return tokenConverter;
	}

}
