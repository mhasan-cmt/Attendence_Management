package org.isfce.pid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// Encodeur pour les passwords lors du login
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean @Profile("dev")
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/h2/**");
		//.antMatchers("/images/**", "/webjars/**", "/js/**","/css/**");
	}

	// A redéfinir pour configurer la manière dont les requètes doivent être
	// sécurisées
	// hasAnyRole==> !! PROF ==> ROLE_PROF (ROLE_ est rajouté automatiquement)
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").defaultSuccessUrl("/", false).and().logout().logoutSuccessUrl("/");

		http.authorizeRequests().antMatchers("/professeur/liste").authenticated()
				.antMatchers("/cours/add", "/cours/*/update", "/cours/*/delete").hasAnyRole("PROF", "ADMIN")
				.antMatchers("/cours/*").authenticated()
				.antMatchers("/professeur/add", "/professeur/*/delete").hasRole("ADMIN")
				.antMatchers("/professeur/*/update").hasAnyRole("PROF", "ADMIN")
				.antMatchers("/professeur/*").hasAnyRole("PROF", "ADMIN")
				.antMatchers("/user/check/*").fullyAuthenticated()
				.antMatchers("/user/*/update").fullyAuthenticated()
				.antMatchers("/").permitAll();
		return http.build();
	}

}
