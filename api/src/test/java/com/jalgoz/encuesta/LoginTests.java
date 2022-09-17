package com.jalgoz.encuesta;

import com.jalgoz.encuesta.models.request.UserLoginRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Le decimos que este será un test
@ActiveProfiles("test")
public class LoginTests {

    private static final String API_LOGIN_URL = "/users/login";

    // Se debe inyectar para poder enviar la petición
    @Autowired
    TestRestTemplate testRestTemplate;

    // Los test siempre deben ser void
    @Test // Para que sea test
    public void postLogin_withoutCredentials_returnUnauthorized() {
        ResponseEntity<Object> response = login(null, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    // Para poder re utilizar se creará este método
    public <T> ResponseEntity<T> login(UserLoginRequestModel model, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_LOGIN_URL, model, responseType);
    }
}
