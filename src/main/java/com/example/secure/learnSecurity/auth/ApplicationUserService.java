package com.example.secure.learnSecurity.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {
	
	private final ApplicationUserDAO applicationUserDao;
	
	@Autowired
	public ApplicationUserService(ApplicationUserDAO applicationUserDao) {
		this.applicationUserDao = applicationUserDao;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return this.applicationUserDao.selectApplicationByUsername(username)
				.orElseThrow( () -> new UsernameNotFoundException(username));
	}

}
