package com.jalgoz.encuesta.services;

import com.jalgoz.encuesta.entities.UserEntity;
import com.jalgoz.encuesta.models.UserRegisterRequestModel;

public interface IUserService {
    public UserEntity createUser(UserRegisterRequestModel user);
}
