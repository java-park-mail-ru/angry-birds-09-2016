package ru.mail.park.main.registration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.database.entities.User;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin(origins = {"http://technoteam.herokuapp.com", "http://127.0.0.1:3000", "http://127.0.0.1:80", "*"})
@RestController
@SuppressWarnings({"unused", "MVCPathVariableInspection"})
public class RegistrationController {

    private final AccountService accountService;
    private final SessionService sessionService;

    @Autowired
    public RegistrationController(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @RequestMapping(path="/api/session", method = RequestMethod.GET)
    public ResponseEntity isAuthenticated(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final User user = sessionService.getUserBySessionId(sessionId);

        if(user != null) {
            return ResponseEntity.ok().body("{\"id\":" + user.getUserId() + '}');
        }

        final Long size = sessionService.getLength();
        return status(HttpStatus.UNAUTHORIZED).body(httpSession.getId());
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody AuthenticationRequest body,
                               HttpSession httpSession) {

        final String login = body.getLogin();
        final String password = body.getPassword();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.EMPTY_FIELDS));
        }

        final User user = accountService.getUser(login);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.WRONG_LOGIN_PASSWORD));
        }

        if(user.matchPassword(password)) {
            sessionService.addUserToSession(httpSession.getId(), user);

            final ObjectMapper mapper = new ObjectMapper();
            final ObjectNode response = mapper.createObjectNode();

            response.put("id", user.getUserId());

            return ResponseEntity.ok(response);
        }

        return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
                RegistrationErrors.WRONG_LOGIN_PASSWORD));
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpSession httpSession) {

        final String sessionId = httpSession.getId();
        final User user = sessionService.getUserBySessionId(sessionId);

        if (user == null) {
            return status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.ACCESS_DENIED));
        }

        sessionService.endSession(sessionId);

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody RegistrationRequest body,
                                   HttpSession httpSession) {

        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();

        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.EMPTY_FIELDS));
        }

        User user = new User(login, email, password);

        user = accountService.addUser(user);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors
                    .getErrorMessage(RegistrationErrors.EXISTING_USER));
        }

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode response = mapper.createObjectNode();

        response.put("id", user.getUserId());

        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = "api/user/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable("id") int id) {
        final User user = accountService.getUser(id);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.WRONG_ID));
        }

        return ResponseEntity.ok(user.toJSON());
    }

    @RequestMapping(path = "api/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity editUser(@PathVariable("id") int id,
                                   @RequestBody RegistrationRequest body,
                                   HttpSession httpSession) {

        final User user = accountService.getUser(id);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.WRONG_ID));
        }

        final User currentUser = sessionService.getUserBySessionId(httpSession.getId());

        if (currentUser == null) {
            return status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.ACCESS_DENIED));
        }

        if (currentUser.equals(user)){
            if (body.getLogin() != null) user.setLogin(body.getLogin());
            if (body.getPassword() != null) user.setPassword(body.getPassword());
            if (body.getEmail() != null) user.setEmail(body.getEmail());

            try {
                accountService.updateUser(user);
            } catch (DataIntegrityViolationException ex) {
                ex.printStackTrace();
                return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors
                        .getErrorMessage(RegistrationErrors.EXISTING_USER));
            }

            return ResponseEntity.ok(user.toJSON());
        }

        return status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
                RegistrationErrors.ACCESS_DENIED));
    }

    @RequestMapping(path = "api/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") int id, HttpSession httpSession) {

        final User user = accountService.getUser(id);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.WRONG_ID));
        }

        final User existingUser = sessionService.getUserBySessionId(httpSession.getId());

        if (!existingUser.equals(user)){
            return status(HttpStatus.UNAUTHORIZED).body(RegistrationErrors.getErrorMessage(
                    RegistrationErrors.ACCESS_DENIED));
        }

        accountService.deleteUser(user);
        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/userlist", method = RequestMethod.GET)
    public ResponseEntity getUserList(HttpSession httpSession) {
        final String sessionId = httpSession.getId();

        final ObjectMapper mapper = new ObjectMapper();

        final ArrayNode userJsonList = mapper.createArrayNode();

        final ArrayList<User> userList = (ArrayList<User>) accountService.getAllUsers();

        for (User user : userList) {
            final ObjectNode entry = mapper.createObjectNode();
            entry.put("login", user.getLogin());
            entry.put("email", user.getEmail());
            entry.put("score", user.getUserId());
            userJsonList.add(entry);
        }

        try {
            return ResponseEntity.ok().body(mapper.writeValueAsString(userJsonList));
        } catch (JsonProcessingException ex) {
            return ResponseEntity.ok().body("{}");
        }
    }
}
