package com.jalgoz.encuesta.services;

import com.jalgoz.encuesta.entities.UserEntity;
import com.jalgoz.encuesta.models.UserRegisterRequestModel;
import com.jalgoz.encuesta.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// Para que sepa es un servicio y así poder injectarlo en el controlador
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Hacemos un contructor de injección
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // Para autenticar y buscar al usuario por su email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        // Preguntamos si el email existe en la DB
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        // En caso de que exista retornamos el email y password
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserEntity createUser(UserRegisterRequestModel user) {
        UserEntity userEntity = new UserEntity();

        BeanUtils.copyProperties(user, userEntity); // Copiamos las propiedades del modelo user a userEntity
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword())); // Copiamos manualmente porque no tienen el mismo nombre

        return userRepository.save(userEntity); // Retornamos el usuario guardado
    }

    @Override
    public UserEntity getUser(String email) {

        return userRepository.findByEmail(email);
    }
}
