package omsu.mim.imit.school.registry.data.repository.security;

import omsu.mim.imit.school.registry.data.entity.security.ImitUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ImitUser, Long> {

    Optional<ImitUser> findByUsername(String username);
}