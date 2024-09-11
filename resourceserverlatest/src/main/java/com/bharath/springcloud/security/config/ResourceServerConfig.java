package com.bharath.springcloud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerConfig {
	
	@Bean
	JwtAuthenticationConverter jwtAuthConverter() {
		
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
	
		
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();	
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		
		return jwtAuthenticationConverter;
		
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.oauth2ResourceServer(oauth->oauth.jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthConverter())));
		http.authorizeHttpRequests(authorize->
		authorize.requestMatchers(HttpMethod.GET,"/couponapi/coupons/{code:^[A-Z]*$}")
		.hasAnyRole("USER","ADMIN")
		.requestMatchers(HttpMethod.POST,"/couponapi/coupons").hasRole("ADMIN"));
		
		http.csrf(csrf->csrf.disable());
		
		return http.build();
		
	}
	
	
}
















