package omsu.mim.imit.school.registry.data.repository;

import omsu.mim.imit.school.registry.data.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID> {

}
