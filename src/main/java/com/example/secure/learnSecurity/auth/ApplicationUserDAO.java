package com.example.secure.learnSecurity.auth;

import java.util.Optional;

public interface ApplicationUserDAO {

	Optional<ApplicationUser> selectApplicationByUsername(String username);
}
