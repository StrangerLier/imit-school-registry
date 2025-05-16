package omsu.mim.imit.school.registry.data.repository;

import omsu.mim.imit.school.registry.data.entity.xml.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HolidayRepository extends JpaRepository<HolidayEntity, UUID> {
}
