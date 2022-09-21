package com.jalgoz.encuesta;

import com.jalgoz.encuesta.models.UserRegisterRequestModel;
import com.jalgoz.encuesta.models.request.UserLoginRequestModel;
import com.jalgoz.encuesta.repositories.UserRepository;
import com.jalgoz.encuesta.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Le decimos que este será un test
@ActiveProfiles("test")
public class LoginTests {

    private static final String API_LOGIN_URL = "/users/login";

    // Se debe inyectar para poder enviar la petición
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach // Se ejecutará antes de cualquier test
    public void cleanup() { // Empezamos cada vez con una base de datos limpia
        userRepository.deleteAll();
    }

    // Los test siempre deben ser void
    @Test // Para que sea test
    public void postLogin_withoutCredentials_returnForbidden() {
        ResponseEntity<Object> response = login(null, Object.class); // Obtenemos la respuesta a la petición de login que hicimos
        System.out.println("Response: " + response);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN); // Tiene que retornarnos el error FORBIDDEN
    }

    @Test // Para que sea test
    public void postLogin_withWrongCredentials_returnForbidden() {
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);

        // Creamos el modelo para loggearse con un email y password incorrectos
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail("asdfs@gmail.com");
        model.setPassword("12345678");

        ResponseEntity<Object> response = login(model, Object.class); // Obtenemos la respuesta a la petición de login que hicimos
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN); // Tiene que retornarnos el error FORBIDDEN
    }

    @Test // Para que sea test
    public void postLogin_withCorrectCredentials_returnOk() {
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);

        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());

        ResponseEntity<Object> response = login(model, Object.class); /// Obtenemos la respuesta a la petición de login que hicimos
        assertEquals(response.getStatusCode(), HttpStatus.OK); // Tiene que retornarnos el error FORBIDDEN
    }

    @Test // Para que sea test
    public void postLogin_withCorrectCredentials_returnAuthToken() {
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);

        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());

        ResponseEntity<Map<String, String>> response = login(model, new ParameterizedTypeReference<Map<String, String>>() {}); /// Obtenemos la respuesta a la petición de login que hicimos
        Map<String, String> body = response.getBody();
        String token = body.get("token");
        assertTrue(token.contains("Bearer")); // Comprobamos que el token este bien
    }

    // Para re utilizar se creará este método que hace la petición de login
    public <T> ResponseEntity<T> login(UserLoginRequestModel model, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_LOGIN_URL, model, responseType); // Dice que enviaremos una petición post al URL con el modelo de datos para login
    }

    // Para re utilizar se creará este método que hace la petición de login pero obteniendo el token
    public <T> ResponseEntity<T> login(UserLoginRequestModel data, ParameterizedTypeReference<T> responseType) {
        HttpEntity<UserLoginRequestModel> entity = new HttpEntity<UserLoginRequestModel>(data, new HttpHeaders()); // Para obtener el token
        return testRestTemplate.exchange(API_LOGIN_URL, HttpMethod.POST, entity, responseType); // Dice que enviaremos una petición post al URL con el modelo de datos para login
    }
}
