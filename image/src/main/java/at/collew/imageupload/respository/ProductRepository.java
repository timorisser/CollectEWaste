package at.collew.imageupload.respository;



import at.collew.imageupload.entity.ProductImage;
import at.collew.imageupload.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<ProductImage,Integer> {
    Optional<ProfileImage> findBypid(int pid);


    List<ProductImage> findAllByPid(int pid);

    @Transactional
    void deleteBypid(int Pid);

    @Transactional
    void deleteAllBypid(int pid);
}
