package com.jalgoz.encuesta.controllers;

import com.jalgoz.encuesta.entities.UserEntity;
import com.jalgoz.encuesta.models.UserRegisterRequestModel;
import com.jalgoz.encuesta.models.responses.UserRest;
import com.jalgoz.encuesta.services.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/* Esta clase lo que hace es manejar la opciones de POST, GET, DELETE, PUT, PATCH que vayan a la dirección
 * /users, toodo lo que hagamos acá se mostrará al momento de hacer un POST al frontEnd
 */

@RestController // Para decirle que esta clase es un controlador
@RequestMapping("/users") // Cual será la ruta de este controllador 
public class UserController { // Este es un controlador REST

    // Hacemos injección de dependencias
    @Autowired
    private final IUserService iUserService;

    // Como el IUserService es private final se necesita el contructor para inicializar el dato
    public UserController (IUserService IUserService) {
        this.iUserService = IUserService;
    }

    @PostMapping() // Este es un endPoint
    public UserRest createUser(@RequestBody @Valid UserRegisterRequestModel userModel) { // @Valid para que utilice la validación del modelo
        // Process POST request
        UserEntity user = iUserService.createUser(userModel); // Cuando se cree el usuario en la base de datos retornará el mismo usuario

        UserRest userRest = new UserRest();

        BeanUtils.copyProperties(user, userRest);
    
        return userRest;
    }
}
