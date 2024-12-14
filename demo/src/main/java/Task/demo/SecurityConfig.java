package Task.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/h2-console/**", "/login", "/error").permitAll() // Salli H2-konsoli ja kirjautumissivut
                        .anyRequest().authenticated() // Muut reitit vaativat autentikoinnin
                )
                .formLogin(form -> form
                        .loginPage("/login") // Mukautettu kirjautumissivu
                        .defaultSuccessUrl("/events", true) // Uudelleenohjaus kirjautumisen jälkeen
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll) // Salli uloskirjautuminen
                .csrf(AbstractHttpConfigurer::disable) // Poista CSRF-suojaus H2-konsolia varten
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)); // Salli kehysten käyttö H2-konsolia varten

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username("admin")
                .password("admin123") // Plain text -salasana
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Yksinkertainen "ei salausta" -toteutus
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString(); // Ei salausta
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword); // Vertailee suoraan
            }
        };
    }
}

