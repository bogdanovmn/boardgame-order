package com.github.bogdanovmn.boardgameorder.web.app.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${contextPath:}")
	private String contextPath;

	private final ProjectUserDetailsService userDetailsService;

	@Autowired
	public WebSecurityConfig(ProjectUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(new Md5PasswordEncoder());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (!contextPath.isEmpty()) {
			contextPath = "/" + contextPath;
		}
		http.authorizeRequests()
				.antMatchers("/registration").anonymous()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/admin/**").hasAuthority("Admin")
				.anyRequest().authenticated()

		.and().formLogin()
			.loginPage(contextPath + "/login")
			.defaultSuccessUrl("/price-list", true)
			.permitAll()

		.and().logout()
			.logoutRequestMatcher(
				new AntPathRequestMatcher(contextPath + "/logout")
			)
			.logoutSuccessUrl(contextPath + "/login")
			.permitAll()

		.and().csrf()
			.disable();
	}
}
