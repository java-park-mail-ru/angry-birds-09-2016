package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicLong;

@CrossOrigin(origins = {"http://technoteam.herokuapp.com", "http://127.0.0.1:3000"})
@RestController
@SuppressWarnings("unused")
public class RegistrationController {

    private final AccountService accountService;
    private final SessionService sessionService;
    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);

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

        Long size = sessionService.getLength();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(httpSession.getId());
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody AuthenticationRequest body,
                               HttpSession httpSession) {

        final String login = body.getLogin();
        final String password = body.getPassword();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        final UserProfile user = accountService.getUser(login);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        if(user.getPassword().equals(password)) {
            sessionService.addUserToSession(httpSession.getId(), user);
            return ResponseEntity.ok(new SuccessResponse(user.getId()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user.getPassword());
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpSession httpSession) {

        final String sessionId = httpSession.getId();
        final UserProfile userProfile = sessionService.getUserBySessionId(sessionId);

        if (userProfile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        final UserProfile existingUser = accountService.getUser(login);

        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        accountService.addUser(ID_GENETATOR.getAndIncrement(), login, password, email);
        return ResponseEntity.ok(new SuccessResponse(ID_GENETATOR.get() - 1));
    }

    @SuppressWarnings("unused")
    private static final class AuthenticationRequest {
        private String login;
        private String password;

        private AuthenticationRequest() {
        }

        private AuthenticationRequest(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    @SuppressWarnings("unused")
    private static final class RegistrationRequest {
        private String login;
        private String password;
        private String email;

        private RegistrationRequest() {

        }

        private RegistrationRequest(String login, String password, String email) {
            this.login = login;
            this.password = password;
            this.email = email;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }

    private static final class SuccessResponse {
        private long id;

        private SuccessResponse(long id) {
            this.id = id;
        }

        @SuppressWarnings("unused")
        public long getId() {
            return id;
        }
    }
}
