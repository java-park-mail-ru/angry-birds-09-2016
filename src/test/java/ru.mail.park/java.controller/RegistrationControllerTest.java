package ru.mail.park.java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.mail.park.database.entities.User;
import ru.mail.park.main.registration.AuthenticationRequest;
import ru.mail.park.main.registration.RegistrationController;
import ru.mail.park.main.registration.RegistrationRequest;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import java.util.ArrayList;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RegistrationControllerTest {

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private SessionService sessionServiceMock;

    @InjectMocks
    RegistrationController registrationController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        Mockito.reset(accountServiceMock, sessionServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    public void loginTest() throws Exception {
        final User user = new User("shrek", "shrek@kek.kek", "kek");

        final AuthenticationRequest auth = new AuthenticationRequest("shrek", "kek");
        final ObjectMapper mapper = new ObjectMapper();
        final String request = mapper.writeValueAsString(auth);

        when(accountServiceMock.getUser("shrek")).thenReturn(user);

        mockMvc.perform(post("/api/session")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(request)
        )
            .andExpect(status().isOk());
    }

    @Test
    public void logoutTest() throws Exception {
        when(sessionServiceMock.getUserBySessionId(anyString())).thenReturn(new User("shrek", "shrek@kek.kek", "kek"));

        mockMvc.perform(delete("/api/session")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void registrationTest() throws Exception {
        final User user = new User("shrek", "shrek@kek.kek", "kek");

        final RegistrationRequest registrationRequest = new RegistrationRequest("shrek", "kek", "shrek@kek.kek");
        final ObjectMapper mapper = new ObjectMapper();
        final String request = mapper.writeValueAsString(registrationRequest);

        when(accountServiceMock.getUser("shrek")).thenReturn(null);
        when(accountServiceMock.addUser(anyObject())).thenReturn(user);

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void userInfoTest() throws Exception {
        final User user1 = new User("shrek", "shrek@kek.kek", "kek");

        final User user2 = new User("kek", "kek@shrek.shrek", "shrek");
        user2.setUserId(1);

        when(accountServiceMock.getUser(0)).thenReturn(user1);
        when(accountServiceMock.getUser(1)).thenReturn(user2);

        mockMvc.perform(get("/api/user/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(0)))
                .andExpect(jsonPath("$.login", equalTo("shrek")));

        mockMvc.perform(get("/api/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.login", equalTo("kek")));
    }

    @Test
    public void editUserTest() throws Exception {
        final User user = new User("shrek", "shrek@kek.kek", "kek");

        when(accountServiceMock.getUser(0)).thenReturn(user);
        when(sessionServiceMock.getUserBySessionId(anyString())).thenReturn(user);

        final RegistrationRequest registrationRequest = new RegistrationRequest("kek", "kek", "kek@shrek.shrek");
        final ObjectMapper mapper = new ObjectMapper();
        final String request = mapper.writeValueAsString(registrationRequest);

        mockMvc.perform(put("/api/user/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(0)))
                .andExpect(jsonPath("$.login", equalTo("kek")))
                .andExpect(jsonPath("$.email", equalTo("kek@shrek.shrek")));
    }

    @Test
    public void deleteUserTest() throws Exception {
        final User user = new User("shrek", "shrek@kek.kek", "kek");

        when(accountServiceMock.getUser(0)).thenReturn(user);
        when(sessionServiceMock.getUserBySessionId(anyString())).thenReturn(user);

        mockMvc.perform(delete("/api/user/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getUserlistTest() throws Exception {
        final User user1 = new User("shrek", "shrek@kek.kek", "kek");
        final User user2 = new User("kek", "kek@shrek.shrek", "shrek");
        final User user3 = new User("faster kek", "higher@kek.kek", "strongerKek");

        user2.setUserId(1);
        user3.setUserId(2);

        final ArrayList<User> userList = new ArrayList<User>();

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        when(accountServiceMock.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/userlist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score", equalTo(0)))
                .andExpect(jsonPath("$[0].login", equalTo("shrek")))
                .andExpect(jsonPath("$[0].email", equalTo("shrek@kek.kek")))
                .andExpect(jsonPath("$[1].score", equalTo(1)))
                .andExpect(jsonPath("$[1].login", equalTo("kek")))
                .andExpect(jsonPath("$[1].email", equalTo("kek@shrek.shrek")))
                .andExpect(jsonPath("$[2].score", equalTo(2)))
                .andExpect(jsonPath("$[2].login", equalTo("faster kek")))
                .andExpect(jsonPath("$[2].email", equalTo("higher@kek.kek")));
    }
}