package com.jalgoz.encuesta.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* Esta clase lo que hace es manejar la opciones de POST, GET, DELETE, PUT, PATCH que vayan a la dirección
 * /users, todo lo que hagamos acá se mostrará al momento de hacer un POST al frontEnd
 */

@RestController // Para decirle que esta clase es un controlador
@RequestMapping("/users") // Cual será la ruta de este controllador 
public class UserController { // Este es un controlador REST
    // POST, GET, DELETE, PUT, PATCH
    @PostMapping() // Este es un endPoint
    public String createUser() {
        // Process POST request
    
        return "Create User 3";
    }
}
