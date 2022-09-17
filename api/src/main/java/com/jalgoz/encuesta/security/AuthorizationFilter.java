package com.jalgoz.encuesta.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

// Esta clase espara autorizar accesos una vez que el usuario se loggee correctamente
public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // Recuperamos el authorization header, ya que cuando hagamos una petición enviaremos ese header
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        // Si es nulo o si no comienze con el TOKEN_PREFIX
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response); // Para que la aplicación siga al siguiente filtro de la petición
        } else {
            UsernamePasswordAuthenticationToken token = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(token); // Le mandamos el token de autentificación del usuario

            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication (HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        // Si el token existe
        if (token != null) {
            // Quitamos el TOKEN_PREFIX
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

            // Ahora verificamos que el token sea valida para esta aplicación
            String emailUser = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token).getBody().getSubject(); // El subject es el email

            // Si esta null es porque el token no es valido
            if (emailUser != null) {
                return new UsernamePasswordAuthenticationToken(emailUser, null, new ArrayList<>());
            } else {
                return null;
            }
        }

        return null;
    }
}
