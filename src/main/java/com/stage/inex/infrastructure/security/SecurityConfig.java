package com.stage.inex.infrastructure.security;
import com.stage.inex.domain.port.PasswordValidator;
import com.stage.inex.domain.port.TokenGenerator;
import com.stage.inex.domain.port.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new PasswordEncoderImp();
    }

    @Bean
    public PasswordValidator passwordValidator(){

        return new PasswordValidatorImp();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing with Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/register").permitAll() // Allow POST to /register
                        .anyRequest().authenticated() // Everything else needs login
                )
                .httpBasic(Customizer.withDefaults()); // Keep Basic Auth enabled

        return http.build();
    }
}
