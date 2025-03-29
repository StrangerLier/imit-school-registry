package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "attendance")
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "class_id")
    private UUID classId;

    @Column(name = "child_id")
    private UUID childId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_attend")
    private Boolean isAttend;
}
