package omsu.mim.imit.school.registry.data.repository;

import java.util.UUID;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {

}