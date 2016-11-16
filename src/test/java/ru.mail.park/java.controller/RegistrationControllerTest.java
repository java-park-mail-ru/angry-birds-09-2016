package ru.mail.park.java.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

@WebMvcTest
@RunWith(SpringRunner.class)
public class RegistrationControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private SessionService sessionService;

    @Autowired
    private MockMvc mockMvc;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}