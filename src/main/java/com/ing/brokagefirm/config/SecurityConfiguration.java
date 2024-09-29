package com.ing.brokagefirm.config;

import com.ing.brokagefirm.model.Customer;
import com.ing.brokagefirm.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomerService customerService;
    private final AppConfiguration appConfiguration;

    public static final String ADMIN = "admin";

    public SecurityConfiguration(CustomerService customerService, AppConfiguration appConfiguration) {
        this.customerService = customerService;
        this.appConfiguration = appConfiguration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/orders/**", "/api/assets/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customerName -> {
            if (ADMIN.equalsIgnoreCase(customerName) || customerService.noDatabaseCustomers()) {
                return new User(
                        ADMIN,
                        new BCryptPasswordEncoder().encode(appConfiguration.getAdminPassword()),
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            }

            Customer customer = customerService.findByName(customerName)
                    .orElseThrow(() -> new UsernameNotFoundException("Customer not found: " + customerName));

            return new User(
                    customer.getName(),
                    customer.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + customer.getRole()))
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
