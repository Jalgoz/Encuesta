package com.jalgoz.encuesta.services;

import com.jalgoz.encuesta.entities.UserEntity;
import com.jalgoz.encuesta.models.UserRegisterRequestModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService { // Le agregamos UserDetailsService para que se puede implementar en la parte de seguridad

    // Para la autenticación viene con UserDetailsService
    public UserDetails loadUserByUsername(String email);

    public UserEntity createUser(UserRegisterRequestModel user);

    public UserEntity getUser(String email);
}
