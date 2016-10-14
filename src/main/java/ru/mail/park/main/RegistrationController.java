package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.main.requests.AuthenticationRequest;
import ru.mail.park.main.requests.RegistrationRequest;
import ru.mail.park.main.responses.SuccessResponse;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin(origins = {"http://technoteam.herokuapp.com", "http://127.0.0.1"})
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
    public ResponseEntity isAuthenticated(HttpSession httpSession) {//JESSIONID is always changing
        final String sessionId = httpSession.getId();
        final UserProfile userProfile = sessionService.getUserBySessionId(sessionId);

        if(userProfile != null) {
            return ResponseEntity.ok().body("{\"id\":" + userProfile.getId() + '}');
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
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Login or(and) password are empty\"}");
        }

        final UserProfile user = accountService.getUser(login);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Wrong login\"}");
        }

        if(user.getPassword().equals(password)) {
            sessionService.addUserToSession(httpSession.getId(), user);
            return ResponseEntity.ok(new SuccessResponse(user.getId()));
        }

        return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Wrong password\"}");
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpSession httpSession) {

        final String sessionId = httpSession.getId();
        final UserProfile userProfile = sessionService.getUserBySessionId(sessionId);

        if (userProfile == null) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"User is not authenticated\"}");
        }

        sessionService.endSession(sessionId);

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody RegistrationRequest body,
                                   HttpSession httpSession) {
        final String sessionId = httpSession.getId();

        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Fields cannot be empty\"}");
        }

        final UserProfile existingUser = accountService.getUser(login);

        if (existingUser != null) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"User already exists\"}");
        }

        final long id = accountService.addUser(login, password, email).getId();
        return ResponseEntity.ok(new SuccessResponse(id));
    }

    @RequestMapping(path = "api/user/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable("id") int id) {
        final UserProfile user = accountService.getUser(id);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Wrong id\"}");
        }

        return ResponseEntity.ok(user.toJSON());
    }

    @RequestMapping(path = "api/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity editUser(@PathVariable("id") int id,
                                   @RequestBody RegistrationRequest body,
                                   HttpSession httpSession) {

        final UserProfile user = accountService.getUser(id);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Wrong id\"}");
        }

        final UserProfile currentUser = sessionService.getUserBySessionId(httpSession.getId());

        if (currentUser == null) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"User is not authenticated\"}");
        }

        if (currentUser.equals(user)){
            user.setLogin(body.getLogin());
            user.setPassword(body.getPassword());
            user.setEmail(body.getEmail());
            return ResponseEntity.ok("{\"id\":" + user.getId() + '}');
        }

        return status(HttpStatus.BAD_REQUEST).body("{\"status\": 403, \"message\": \"Access denied\"}");
    }

    @RequestMapping(path = "api/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") int id, HttpSession httpSession) {

        final UserProfile user = accountService.getUser(id);

        if (user == null) {
            return status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Wrong id\"}");
        }

        final UserProfile userExisting = sessionService.getUserBySessionId(httpSession.getId());

        if (!userExisting.equals(user)){
            return status(HttpStatus.BAD_REQUEST).body("{\"status\": 403, \"message\": \"Access denied\"}");
        }

        accountService.deleteUser(user);
        return ResponseEntity.ok("{}");
    }
}
