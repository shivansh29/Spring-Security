package com.example.secure.learnSecurity.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


// to verify the token that we get from client
public class JwtTokenVerifier extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader("Authorization");
		if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeader.replace("Bearer ", "");
		try {

			String secretKey = "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss";
			Jws<Claims>  claimsJws = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			// We use this method to parse Jws because when we create token we use compact() which converts JWT to JWS So we need to parse JWS not JWT
			.parseClaimsJws(token);  
			
			// Getting body of token
			Claims body = claimsJws.getBody();
			
			String username = body.getSubject();
			
			// getting authorities from token body
			List<Map<String,String>> authorities= (List<Map<String,String>>) body.get("authorities");
			
			Set<SimpleGrantedAuthority> simpleAuth=authorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());
			
			Authentication authenticate = new UsernamePasswordAuthenticationToken(
					username, null, simpleAuth
					);
			
			// setting authentication which we got by token body
			SecurityContextHolder.getContext().setAuthentication(authenticate);
		}
		catch(JwtException e) {
			throw new IllegalStateException(String.format("This Token %s cannot be trusted",token));
		}
		
		filterChain.doFilter(request, response);
	}

}
