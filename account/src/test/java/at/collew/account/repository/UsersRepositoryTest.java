package at.collew.account.repository;

import at.collew.account.model.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Patrick Stadt
 * @version 2
 */


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class UsersRepositoryTest {

    @Autowired
    private UsersRepository repository;

    @Test
    void successfullyFindUserByEmail() {
        Users jebote = new Users("Jebote", "Karrotte", "JeboteKarrotte@gmail.com", "+43 583401375", "password", false, "");
        this.repository.save(jebote);
        assertEquals(jebote,this.repository.findByEmail("JeboteKarrotte@gmail.com"));
    }

    @Test
    void notSuccessfullyFindUserByEmail() {
        assertNull(this.repository.findByEmail("JeboteKarrotte@gmail.com"));
    }

    @Test
    void userExistsByEmail() {
        Users hefty = new Users("Hefty", "Boy", "HeftyBoy@gmail.com", "+43 583401375", "password", false, "");
        this.repository.save(hefty);
        assertTrue(this.repository.existsByEmail("HeftyBoy@gmail.com"));
    }

    @Test
    void userDoesNotExistsByEmail() {
        assertFalse(this.repository.existsByEmail("DoesNotExists@gmail.com"));
    }

    @Test
    void notSuccessfullFindById() {
        assertNull(this.repository.findById(69));
    }
}