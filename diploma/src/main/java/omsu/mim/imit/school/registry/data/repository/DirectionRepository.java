package omsu.mim.imit.school.registry.data.repository;

import omsu.mim.imit.school.registry.data.entity.DirectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DirectionRepository extends JpaRepository<DirectionEntity, UUID> {
}
