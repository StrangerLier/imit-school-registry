package omsu.mim.imit.school.registry.data.repository.security;

import omsu.mim.imit.school.registry.data.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findAllByUserId(UUID userId);
}