package com.jalgoz.encuesta.models.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginRequestModel {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;
}
