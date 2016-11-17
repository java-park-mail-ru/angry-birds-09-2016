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
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

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
}