package com.microservicios.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired 
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//Permisos que tendran los endpoints del servidor de autenticacion 
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}
	
	//Se autentican las aplicaciones que consumen los microservicios
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("fontEndAngularApp")//username de la app
			.secret(passwordEncoder.encode("angular"))//password de la app
			.scopes("read","write")//permisos de la app
			.authorizedGrantTypes("password","refresh_token")//autorizacion por contraseña y el token se refresca cuando esta a punto de terminar
			.accessTokenValiditySeconds(3600)
			.refreshTokenValiditySeconds(3600)
			.and()
			.withClient("fontEndAndroidApp")//username de la app
			.secret(passwordEncoder.encode("android"))//password de la app
			.scopes("read","write")//permisos de la app
			.authorizedGrantTypes("password","refresh_token")//autorizacion por contraseña y el token se refresca cuando esta a punto de terminar
			.accessTokenValiditySeconds(3600)
			.refreshTokenValiditySeconds(3600);
	}
	
	//endpoint encargado de generar el token
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
			.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter());
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("ageofempires2");
		return tokenConverter;
	}
	
	

}
