package omsu.mim.imit.school.registry.data.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChildRepository extends JpaRepository<ChildEntity, UUID> {

    @Query("select c from child c "
        + "where (:classNumber is null or c.classNumber = :classNumber) "
        + "and (:birthDate is null or c.birthDate = :birthDate) "
        + "and (:surname is null or c.surname = :surname) "
        + "and (:school is null or c.school = :school)"
        + "and (:groupId is null or c.groupId = :groupId)")
    List<ChildEntity> filter(@Param("classNumber") String classNumber,
                             @Param("birthDate") LocalDate birthDate,
                             @Param("surname") String surname,
                             @Param("school") String school,
                             @Param("groupId") UUID groupId);

    @Query("select c from child c "
        + "where (:name is null or c.name = :name) "
        + "and (:secondName is null or c.birthDate = :secondName) "
        + "and (:surname is null or c.surname = :surname) "
        + "and (:birthDate is null or c.birthDate = :birthDate) "
        + "and (:groupId is null or c.groupId = :groupId)")
    Optional<ChildEntity> findForDuble(@Param("name") String name,
                                 @Param("secondName") String secondName,
                                 @Param("surname") String surname,
                                 @Param("birthDate") LocalDate birthDate,
                                 @Param("groupId") UUID groupId);
}
