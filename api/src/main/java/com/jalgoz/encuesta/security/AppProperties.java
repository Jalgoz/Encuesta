package com.jalgoz.encuesta.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

// Pata obtener el token secreto desde application.properties
@Component
public class AppProperties {

    // Le estamos inyectando la clase Enviroment
    @Autowired
    private Environment env;

    public String getTokenSecret() {
        return env.getProperty("tokenSecret");
    }
}
