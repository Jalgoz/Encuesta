package com.jalgoz.encuesta.exceptions;

import com.jalgoz.encuesta.models.responses.ValidationErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class}) // Sobreescribimos la excepción cuando hay un error de validación
    public ResponseEntity<Object> handleValidationErrorException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>(); // Creamos una estructura de datos, seran los errores que retornaran al cliente

        for (Object error: ex.getBindingResult().getAllErrors()) { // Iteramos sobre todos los errores que se pueden generan en la excepción de validación
            String fieldName = ((FieldError) error).getField(); // Traemos el error del campo
            String errorMessage = ((FieldError) error).getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }

        // Creamos una instancia de ValidationErrors porque se necesita retornar un objeto(clase)
        ValidationErrors validationErrors = new ValidationErrors(errors, new Date());

        // Retornamos un nuevo responseEntity con el objeto, un nuevo HttpHeaders, y el error que deberia salir en caso de no estar bien la validación
        // que en este caso es HttpStatus.BAD_REQUEST = 400
        return new ResponseEntity<>(validationErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
