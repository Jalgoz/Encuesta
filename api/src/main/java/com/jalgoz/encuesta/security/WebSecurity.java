package com.jalgoz.encuesta.security;

import com.jalgoz.encuesta.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Debido que implementamos spring boot starter security cualquier petición es rechazada por seguridad
@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // Para permitir las peticiones
//    @Bean
//    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable();
//
//        // Para permitir que todos las peticiones de /users sean permitidas
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/users")
//                .permitAll()
//                .anyRequest()
//                .permitAll();
//
//        // Para decirle que una vez iniciada sesión utilice tokens y no cookies
//        http.addFilter(getAuthenticationFilter()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        return http.build();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.authorizeRequests().antMatchers(HttpMethod.POST, "/users")
                .permitAll().anyRequest().authenticated();

        // Aquí se agregan lo filtros de seguridad
        http.addFilter(getAuthenticationFilter()).addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // Para loggearse, le decimos que encriptador usamos
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    // Para redirigir una vez autenticado
    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager());

        authenticationFilter.setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
        return authenticationFilter;
    }
}
