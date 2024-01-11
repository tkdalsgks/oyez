package kr.oyez.security.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kr.oyez.security.handler.CustomAuthenticationFailureHandler;
import kr.oyez.security.handler.CustomAuthenticationSuccessHandler;
import kr.oyez.security.service.CustomOAuth2UserService;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
	        	.csrf(csrf -> csrf.disable())
	        	.httpBasic(AbstractHttpConfigurer::disable)
	        	.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
	        	.authorizeHttpRequests(requests -> requests
	        			//.requestMatchers("/swagger-ui/**", "/", "/join", "/check/**", "/find/**", "/certified/**").permitAll()
						//.requestMatchers("/free/**", "/review/**", "/comments/**", "/upload/**").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPERADMIN")
						//.requestMatchers("/chat/**", "/points/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPERADMIN")
						//.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERADMIN")
		                .anyRequest().permitAll()
	            )
	        	.sessionManagement(session -> session
	        			.maximumSessions(1)
						.maxSessionsPreventsLogin(false)
						.expiredUrl("/")
						.sessionRegistry(sessionRegistry())
	        	)
	        	.logout(logout -> logout
	        			.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)
	        	.formLogin(login -> login
	        			.loginPage("/")
		        		.loginProcessingUrl("/login")
						.defaultSuccessUrl("/")
						.failureUrl("/")
						.usernameParameter("memberId")
						.passwordParameter("memberPwd")
						.successHandler(customAuthenticationSuccessHandler)
						.failureHandler(customAuthenticationFailureHandler)
						.permitAll()
				)
	        	.oauth2Login(oauth2 -> oauth2
		        		.loginPage("/")
						.defaultSuccessUrl("/")
						.permitAll()
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)
						)
				);
		
		return http.build();
	}
	
	@Bean
	protected WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**", "/error/**");
	}
	
	@Bean
	protected BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	protected SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	
	/**
	 *  Spring Security 5.7.0 Update
	 *  https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
	 */
}
