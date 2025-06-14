package omsu.mim.imit.school.registry.data.repository;

import java.util.List;
import java.util.UUID;
import jakarta.transaction.Transactional;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
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

    @Modifying
    @Transactional
    @Query("""
            update group_info
            set classNumber = :#{#group.classNumber},
                teacherId = :#{#group.teacherId},
                directionId = :#{#group.directionId},
                address = :#{#group.address},
                listenersAmount = :#{#group.listenersAmount},
                approvedListeners = :#{#group.approvedListeners},
                time = :#{#group.time},
                dayOfWeek = :#{#group.dayOfWeek},
                assistantsIds = :#{#group.assistantsIds}
            where id = :#{#group.id}
    """)
    void update(@Param("group") GroupEntity group);

    @Query("""
            select c from child c where groupId in (:#{#groupIds})
    """)
    List<ChildEntity> getChildInGroups(List<UUID> groupIds);

    @Query("""
            select g from group_info g where directionId in (:#{#directionsIds})
    """)
    List<GroupEntity> getByDirectionsIds(List<UUID> directionsIds);

    @Modifying
    @Transactional
    @Query("""
            update group_info
            set assistantsIds = :assistantsIds
            where id = :groupId
    """)
    void updateAssistants(UUID groupId, String assistantsIds);

    @Modifying
    @Transactional
    @Query("""
            update group_info
            set status = :status
            where id = :groupId
    """)
    void updateStatus(UUID groupId, String status);

}