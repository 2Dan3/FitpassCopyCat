package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(m.*) FROM manages m WHERE m.facility_id = :facilityId and m.user_id = :userId")
    int findManager(@Param("facilityId") Long facilityId, @Param("userId") Long userId);
}
