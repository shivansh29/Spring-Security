package com.example.secure.learnSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.secure.learnSecurity.auth.ApplicationUserService;
import com.example.secure.learnSecurity.jwt.JwtTokenVerifier;
import com.example.secure.learnSecurity.jwt.JwtUsernameAndPasswordAuthenticationFilter;

import static com.example.secure.learnSecurity.config.ApplicationUserPermission.*;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserService applicationUserService;
	
	
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
	}

	@Override
	protected void configure(HttpSecurity  http) throws Exception {
		http.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()))
		.addFilterAfter(new JwtTokenVerifier(), JwtUsernameAndPasswordAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers("/","index")
		.permitAll()
		.antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
		.antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())
		.anyRequest()
		.authenticated();
		
		
		// Without JWT
		/*http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/","index")
		.permitAll()
		.antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
		.antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/login").permitAll().defaultSuccessUrl("/courses",true)
		.and().rememberMe()
		.tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
		.key("somethingsecure")
		.and()
		.logout().logoutUrl("/logout")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
		.clearAuthentication(true)
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID","remember-me")
		.logoutSuccessUrl("/login");  */
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(this.applicationUserService);
		return provider;
	}
}
