package com.jalgoz.encuesta.validators;

import com.jalgoz.encuesta.annotations.UniqueEmail;
import com.jalgoz.encuesta.entities.UserEntity;
import com.jalgoz.encuesta.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        UserEntity user = userRepository.findByEmail(value);

        // Si el usuario existe
        if (user == null) {
            return true;
        }

        return false;
    }
}
