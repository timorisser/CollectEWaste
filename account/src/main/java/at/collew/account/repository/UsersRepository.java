package at.collew.account.repository;

import at.collew.account.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repository for user
 * @author Patrick Stadt
 * @version 2
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    /**
     * Find by email
     * @param email email of user
     * @return the user
     */
    Users findByEmail(String email);

    /**
     * Get a boolean when email exist in database
     * @param email email user
     * @return a boolean value if the email already exists in database
     */
    Boolean existsByEmail(String email);

    /**
     * Find by ID
     * @param id id of user
     * @return user that is mapped to the id
     */
    Users findById(int id);
}