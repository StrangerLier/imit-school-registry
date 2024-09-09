package omsu.mim.imit.school.registry.data.repository;

import java.util.UUID;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<ChildEntity, UUID> {
}
