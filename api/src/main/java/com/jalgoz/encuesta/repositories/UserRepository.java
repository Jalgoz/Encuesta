package com.jalgoz.encuesta.repositories;

import com.jalgoz.encuesta.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Creamos esta clase utilizar los métodos CRUD y asi poder guardar o encontrar datos
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> { // Le mandamos la entidad del usuario y la clave que es el id

}
