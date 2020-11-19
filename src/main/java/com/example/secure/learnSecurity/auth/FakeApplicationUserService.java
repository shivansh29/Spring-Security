package com.example.secure.learnSecurity.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.secure.learnSecurity.config.ApplicationUserRole;
import com.google.common.collect.Lists;

@Repository("fake")
public class FakeApplicationUserService implements ApplicationUserDAO {
	
	private final PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public FakeApplicationUserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<ApplicationUser> selectApplicationByUsername(String username) {
		
		return getApplicationUser().stream()
				.filter( (applicationUser) -> username.equals(applicationUser.getUsername()) )
				.findFirst();
	}

	public List<ApplicationUser> getApplicationUser(){
		List<ApplicationUser> applicationUser =  Lists.newArrayList(
				new ApplicationUser(
						ApplicationUserRole.STUDENT.getGrantedAuthorities(),
						passwordEncoder.encode("password"),
						"shivansh",
						true, true, true, true
						),
				new ApplicationUser(
						ApplicationUserRole.ADMIN.getGrantedAuthorities(),
						passwordEncoder.encode("password"),
						"admin",
						true, true, true, true
						),
				new ApplicationUser(
						ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities(),
						passwordEncoder.encode("password"),
						"trainee",
						true, true, true, true
						)
				);
		return applicationUser;
	}
}
