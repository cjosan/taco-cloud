package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new StandardPasswordEncoder("53cr3t");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(encoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.authorizeRequests()
//				.antMatchers("/design", "/orders")
//				.hasRole("ROLE_USER")
//				.antMatchers("/", "/**").permitAll();

		http
				.authorizeRequests()
				.antMatchers("/design", "/order")
					.access("hasRole('ROLE_USER')")
				.antMatchers("/", "/**")
					.access("permitAll")
				.and()
					.formLogin()
					.loginPage("/login")
				.and()
					.logout()
					.logoutSuccessUrl("/")
				.and()
					.csrf()
//				.disable();
					.ignoringAntMatchers("/h2-console/**")
				.and()
					.headers()
					.frameOptions()
					.sameOrigin();
	}
}
