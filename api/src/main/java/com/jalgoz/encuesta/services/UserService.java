package com.jalgoz.encuesta.services;

import com.jalgoz.encuesta.entities.UserEntity;
import com.jalgoz.encuesta.models.UserRegisterRequestModel;
import com.jalgoz.encuesta.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

// Para que sepa es un servicio y así poder injectarlo en el controlador
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    // Hacemos un contructor de injección
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity createUser(UserRegisterRequestModel user) {
        UserEntity userEntity = new UserEntity();

        BeanUtils.copyProperties(user, userEntity); // Copiamos las propiedades del modelo user a userEntity
        userEntity.setEncryptedPassword(user.getPassword()); // Copiamos manualmente porque no tienen el mismo nombre

        return userRepository.save(userEntity); // Retornamos el usuario guardado
    }
}
