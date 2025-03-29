package omsu.mim.imit.school.registry.data.repository;

import jakarta.transaction.Transactional;
import omsu.mim.imit.school.registry.data.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {

    @Modifying
    @Transactional
    @Query("""
            delete class
            where groupId = :groupId
    """)
    void deleteAllByGroupId(@Param("groupId") UUID groupId);

    @Query("""
            select c from class c where groupId = :groupId
    """)
    List<ClassEntity> getClassesInGroup(@Param("groupId") UUID groupId);

    @Modifying
    @Transactional
    @Query("""
            update class
            set theme = :theme
            where id = :classId
    """)
    void setTheme(@Param("classId") UUID classId,
                  @Param("theme") String theme);
}


