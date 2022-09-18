package com.jalgoz.encuesta;

import com.jalgoz.encuesta.entities.UserEntity;
import com.jalgoz.encuesta.models.UserRegisterRequestModel;
import com.jalgoz.encuesta.models.request.UserLoginRequestModel;
import com.jalgoz.encuesta.models.responses.UserRest;
import com.jalgoz.encuesta.models.responses.ValidationErrors;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Le decimos que este será un test
@ActiveProfiles("test")
public class UserControllerTests {

    private static final String API_URL = "/users";
    private static final String API_LOGIN_URL = "/users/login";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_withoutName_returnBadRequest() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        model.setName("");
        ResponseEntity<Object> response = create(model, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_withoutData_returnBadRequest() {
        UserRegisterRequestModel model = new UserRegisterRequestModel();
        ResponseEntity<Object> response = create(model, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_withoutPassword_returnBadRequest() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        model.setPassword("");
        ResponseEntity<Object> response = create(model, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_withoutEmail_returnBadRequest() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        model.setEmail("");
        ResponseEntity<Object> response = create(model, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    // Para ver que este mostrando los errores de validación correctos
    @Test
    public void createUser_withoutData_returnValidationErrors() {
        UserRegisterRequestModel model = new UserRegisterRequestModel();
        ResponseEntity<ValidationErrors> response = create(model, ValidationErrors.class);

        Map<String, String> errors = response.getBody().getErrors();

        assertEquals(errors.size(), 3);
    }

    @Test
    public void createUser_withoutName_returnValidationError() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        model.setName(null);
        ResponseEntity<ValidationErrors> response = create(model, ValidationErrors.class);

        Map<String, String> errors = response.getBody().getErrors();

        assertTrue(errors.containsKey("name"));
    }

    @Test
    public void createUser_withoutEmail_returnValidationError() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        model.setEmail(null);
        ResponseEntity<ValidationErrors> response = create(model, ValidationErrors.class);

        Map<String, String> errors = response.getBody().getErrors();

        assertTrue(errors.containsKey("email"));
    }

    @Test
    public void createUser_withPassword_returnValidationError() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        model.setPassword(null);
        ResponseEntity<ValidationErrors> response = create(model, ValidationErrors.class);

        Map<String, String> errors = response.getBody().getErrors();

        assertTrue(errors.containsKey("password"));
    }

    @Test
    public void createUser_withEmailUserExistent_returnValidationError() {
        UserRegisterRequestModel model = TestUtil.createValidUser();

        create(model, UserRest.class);
        ResponseEntity<ValidationErrors> response = create(model, ValidationErrors.class);

        Map<String, String> errors = response.getBody().getErrors();

        assertTrue(errors.containsKey("email"));
    }

    @Test
    public void createUser_withValidUser_returnOK() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        ResponseEntity<UserRest> response = create(model, UserRest.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void createUser_withValidUser_returnUserRest() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        ResponseEntity<UserRest> response = create(model, UserRest.class);

        assertEquals(response.getBody().getName(), model.getName());
    }

    @Test
    public void createUser_withValidUser_saveUserDB() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        ResponseEntity<UserRest> response = create(model, UserRest.class);

        UserEntity userDB = userRepository.findById(response.getBody().getId());

        assertNotNull(userDB);
    }

    @Test
    public void createUser_withValidUser_saveHashPasswordDB() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        ResponseEntity<UserRest> response = create(model, UserRest.class);

        UserEntity userDB = userRepository.findById(response.getBody().getId());

        assertNotEquals(model.getPassword(), userDB.getEncryptedPassword());
    }

    @Test
    public void createUser_withEmailUserExistent_returnBadRequest() {
        UserRegisterRequestModel model = TestUtil.createValidUser();
        create(model, UserRest.class);
        ResponseEntity<UserRest> response = create(model, UserRest.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getUser_withoutToken_returnForbidden() {
        ResponseEntity<Object> response = getUser(null, new ParameterizedTypeReference<Object>(){});

        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    public void getUser_withoutToken_returnOK() {
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);

        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());

        ResponseEntity<Map<String, String>> responseLogin = login(model, new ParameterizedTypeReference<Map<String, String>>() {}); /// Obtenemos la respuesta a la petición de login que hicimos
        Map<String, String> body = responseLogin.getBody();
        String token = body.get("token").replace("Bearer", "");

        ResponseEntity<UserRest> response = getUser(token, new ParameterizedTypeReference<UserRest>(){});
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getUser_withoutToken_returnRest() {
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);

        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());

        ResponseEntity<Map<String, String>> responseLogin = login(model, new ParameterizedTypeReference<Map<String, String>>() {}); /// Obtenemos la respuesta a la petición de login que hicimos
        Map<String, String> body = responseLogin.getBody();
        String token = body.get("token").replace("Bearer", "");

        ResponseEntity<UserRest> response = getUser(token, new ParameterizedTypeReference<UserRest>(){});
        assertEquals(user.getName(), response.getBody().getName());
    }

    // Para re utilizar se creará este método que hará la petición de crear un nuevo usuario
    public <T> ResponseEntity<T> create(UserRegisterRequestModel model, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_URL, model, responseType); // Dice que enviaremos una petición post al URL con el modelo de datos para login
    }

    public <T> ResponseEntity<T> getUser(String token, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entity = new HttpEntity<Object>(null, headers); // Para obtener el token
        return testRestTemplate.exchange(API_URL, HttpMethod.GET, entity, responseType); // Dice que enviaremos una petición post al URL con el modelo de datos para login
    }

    // Para re utilizar se creará este método que hace la petición de login pero obteniendo el token
    public <T> ResponseEntity<T> login(UserLoginRequestModel data, ParameterizedTypeReference<T> responseType) {
        HttpEntity<UserLoginRequestModel> entity = new HttpEntity<UserLoginRequestModel>(data, new HttpHeaders()); // Para obtener el token
        return testRestTemplate.exchange(API_LOGIN_URL, HttpMethod.POST, entity, responseType); // Dice que enviaremos una petición post al URL con el modelo de datos para login
    }
}
