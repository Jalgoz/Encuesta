package com.jalgoz.encuesta.entities;

import lombok.Data;

import javax.persistence.*;

// Acá crearemos la tabla de users
@Entity(name="users") // Para que sea una entidad tenemos que pasarle @Entity
@Data // Para no usar getters y setters
public class UserEntity {

    @Id // Le decimos que será la columna ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para que se auto genere el ID
    private long id;

    @Column(nullable = false, length = 255) // Para decirle que será una columna de nuestra tabla users
    private String name;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String encryptedPassword;
}
