package at.collew.account.service.impl;

import at.collew.account.dto.UserDto;
import at.collew.account.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Patrick Stadt
 * @version 1
 */

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class UsersServiceImplTest {

    @Autowired
    @MockBean
    private UsersServiceImpl usersService;

    @Autowired
    private UsersRepository userRepository;


    /*@Test
    void successfullCreateUser() {
        //Users jebote = new Users("Jebote", "Karrotte", "JeboteKarrotte@gmail.com", "+43 583401375", "password", false, "");
        UserDto help = new UserDto("Jebote", "Karrotte", "JeboteKarrotte@gmail.com", "+43 583401375", "password", false, "");
        this.usersService.createUser(help);

        assertSame("JeboteKarrotte@gmail.com",this.usersService.findUserByEmail("JeboteKarrotte@gmail.com").getEmail());
    }

    @Test
    void notSuccessfullCreateUser() {
        UserDto test = new UserDto();
        test.setEmail("test@gmail.com");
        this.usersService.createUser(test);
        assertNull(this.usersService.findUserByEmail("test@gmail.com"));
    }

    @Test
    void successfullFindUserByEmail() {
        UserDto jebote = new UserDto("Jebote", "Karrotte", "JeboteKarrotte@gmail.com", "+43 583401375", "password", false, "");
        this.usersService.createUser(jebote);
        this.usersService.findUserByEmail("JeboteKarrotte@gmail.com");
        assertEquals("JeboteKarrotte@gmail.com", this.usersService.findUserByEmail("JeboteKarrotte@gmail.com").getEmail());
    }*/

}