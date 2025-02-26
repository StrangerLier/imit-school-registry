package omsu.mim.imit.school.registry.data.repository;

import java.util.UUID;
import jakarta.transaction.Transactional;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {

    @Modifying
    @Transactional
    @Query("""
            update group_info
            set approvedListeners = approvedListeners + 1
            where id = :groupId
    """)
    void increaseListenersAmount(@Param("groupId") UUID groupId);

}