package com.ivs.usermanager.config;

import com.ivs.usermanager.modules.auth.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Marks this class as a source of bean definitions
@RequiredArgsConstructor // Automatically injects final fields (authRepository) via constructor
public class ApplicationConfig {

    private final AuthRepository authRepository;

    /*
     * Define the password encoder bean using BCrypt.
     * It will be used to hash passwords during registration and compare them during
     * login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * UserDetailsService is a core interface in Spring Security.
     * It defines how to load user-specific data from our database using email.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> authRepository.findByEmail(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        /* Check if role exists to avoid NullPointerException */
                        .authorities(user.getRole() != null ? user.getRole().getName() : "USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    /*
     * The AuthenticationProvider is responsible for the actual authentication
     * logic.
     * It uses the UserDetailsService to find the user and PasswordEncoder to check
     * the password.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /*
     * Expose the AuthenticationManager bean.
     * We will use this in AuthService to manually authenticate users during login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}