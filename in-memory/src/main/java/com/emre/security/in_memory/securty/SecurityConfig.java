package com.emre.security.in_memory.securty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService users(){
        UserDetails user1= User.builder()
                .username("Emre")
                .password(passwordEncoder().encode("pass"))
                .roles("USER")
                .build();

        UserDetails admin= User.builder()
                .username("İbrahim")
                .password(passwordEncoder().encode("pass"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1,admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .headers(x->x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x-> x.requestMatchers("/public/**","/auth/**").permitAll())  //hasRole("USER") //.hasAnyRole() //.hasAuthority()
                .authorizeHttpRequests(x->x.requestMatchers("/private/user/**").hasRole("USER"))
                .authorizeHttpRequests(x->x.requestMatchers("/private/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests(x->x.anyRequest().authenticated())
                .httpBasic((Customizer.withDefaults()));
        return security.build();
    }
}
