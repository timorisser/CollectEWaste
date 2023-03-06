package at.collew.imageupload.respository;



import at.collew.imageupload.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileImage,Integer> {
    Optional<ProfileImage> findByuid(int uid);

    @Transactional
    void deleteByuid(int uid);


}
