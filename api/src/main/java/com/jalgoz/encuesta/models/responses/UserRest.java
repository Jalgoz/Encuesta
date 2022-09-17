package com.jalgoz.encuesta.models.responses;

import lombok.Data;

import javax.persistence.Column;

@Data
public class UserRest {

    private long id;

    private String name;

    private String email;
}
