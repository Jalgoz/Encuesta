package com.jalgoz.encuesta;

import com.jalgoz.encuesta.models.UserRegisterRequestModel;

import java.util.Random;

// Para crear usuarios para las pruebas
public class TestUtil {

    // Con este método crearemos los usuarios para las pruebas
    public static UserRegisterRequestModel createValidUser() {
        UserRegisterRequestModel user = new UserRegisterRequestModel();
        user.setEmail(generateRandomSpring(8) + "@gmail.com");
        user.setName(generateRandomSpring(7));
        user.setPassword(generateRandomSpring(8));
        return user;
    }

    public static String generateRandomSpring(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
