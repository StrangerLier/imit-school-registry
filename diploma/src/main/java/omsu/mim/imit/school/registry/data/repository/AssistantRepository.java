package omsu.mim.imit.school.registry.data.repository;

import omsu.mim.imit.school.registry.data.entity.AssistantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssistantRepository extends JpaRepository<AssistantEntity, UUID> {
}
