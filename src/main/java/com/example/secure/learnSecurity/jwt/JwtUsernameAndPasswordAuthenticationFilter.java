package com.example.secure.learnSecurity.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authManager;
	
	
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager) {
		super();
		this.authManager = authManager;
	}


	@Override
	public Authentication attemptAuthentication( HttpServletRequest request, 
		      HttpServletResponse response) throws AuthenticationException {
	
		try {
			// getting the user request or getting the credentials
			//like in the diagram    sending the credentials
			UsernameAndPasswordRequest authRequest= new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordRequest.class);
			
			//authenticating the request 
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					authRequest.getUsername(),authRequest.getPassword()
					);
			
			return authManager.authenticate(authentication);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	
	//  This runs after attemptAuthentication method runs successfully
	@Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,FilterChain filter, Authentication authResult)
            throws IOException, ServletException{
		
		// Generating a token
		String key = "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss";
		String token=  Jwts.builder().setSubject(authResult.getName())
		.claim("authorities", authResult.getAuthorities())
		.setIssuedAt(new Date())
		.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
		.signWith(Keys.hmacShaKeyFor(key.getBytes()))
		.compact();
		
		//sending the token back
		response.addHeader("Authorization", "Bearer "+token);
	}
}
