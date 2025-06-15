package omsu.mim.imit.school.registry.data.repository;

import omsu.mim.imit.school.registry.data.entity.RiskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RiskRepository extends JpaRepository<RiskEntity, UUID> {

    @Query("""
            select r from risk r where groupId in (:#{#groupIds})
    """)
    List<RiskEntity> findByGroupIds(List<UUID> groupIds);

    @Query("""
            select r from risk r 
            where reason = 'UNFILLED_GROUP'
    """)
    List<RiskEntity> getUnfilledGroupRisks();

}
