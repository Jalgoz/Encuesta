package com.jalgoz.encuesta.models;

import com.jalgoz.encuesta.annotations.UniqueEmail;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

// Este será el modelo de tipo de datos que tendremos que mandar para crear a un usuario, en el userController le decimos que use este modelo
@Data
public class UserRegisterRequestModel {

    @NotEmpty
    @Size(min = 2, max = 100)
    private String name;

    @NotEmpty
    @Email
    @UniqueEmail
    private String email;

    @NotEmpty
    @Size(min = 8, max = 50)
    private String password;
}
