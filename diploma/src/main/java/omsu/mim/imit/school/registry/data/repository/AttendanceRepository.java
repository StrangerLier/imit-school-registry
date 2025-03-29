package omsu.mim.imit.school.registry.data.repository;

import jakarta.transaction.Transactional;
import omsu.mim.imit.school.registry.data.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, UUID> {

    @Modifying
    @Transactional
    @Query("""
            delete attendance
            where classId = :classId
    """)
    void deleteAllByClassId(@Param("classId") UUID classId);

    @Query("select a from attendance a "
            + "where a.classId = :classId")
    List<AttendanceEntity> getAllByClassId(@Param("classId") UUID classId);

    @Modifying
    @Transactional
    @Query("""
            update attendance
            set isAttend = :isAttend,
            comment = :comment
            where id = :attendanceId
    """)
    void setAttendance(@Param("attendanceId") UUID attendanceId,
                       @Param("isAttend") Boolean isAttend,
                       @Param("comment") String comment);
}
