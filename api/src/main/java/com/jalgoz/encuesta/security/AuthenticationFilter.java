package com.jalgoz.encuesta.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalgoz.encuesta.models.request.UserLoginRequestModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Para autenticarnos
    @Override
    public Authentication attemptAuthentication (HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Vamos a obtener los datos de la petición pero solo los que se necesital en la clase UserLoginRequestModel
            UserLoginRequestModel userModel = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);

            // Nos autenticamos con email y password, ademas estamos mandando una serie de opciones vacias (new ArrayList<>())
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userModel.getEmail(), userModel.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Aquí hacemos la acción en caso de que la autenticación sea correcta
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((User)authResult.getPrincipal()).getUsername(); // Obtenemos el email de usuario autenticado

        // Generamos el token
        String token = Jwts.builder().setSubject(email).setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact(); // Se tiene que mejorar la clave secreta

        // System.out.println(token);

        String data = new ObjectMapper().writeValueAsString(Map.of("token", SecurityConstants.TOKEN_PREFIX + token));

        // Para retornar el token una vez iniciemos sesión
        // Es necesario retornar el token para la parte del cliente
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(data);
        response.flushBuffer();
    }
}
