package at.collew.account.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Patrick Stadt
 * @version 1
 */

@WebAppConfiguration
@AutoConfigureMockMvc
@SpringBootTest
@DataJpaTest
class UserControllerTests {

    private MockMvc mockMVC;

    @Test
    void signUp() {

    }

}