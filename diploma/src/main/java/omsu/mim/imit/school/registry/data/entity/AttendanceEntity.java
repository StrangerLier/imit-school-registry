package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "attendance")
public class AttendanceEntity extends BaseEntity {
    @Column(name = "class_id")
    private UUID classId;

    @Column(name = "child_id")
    private UUID childId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_attend")
    private Boolean isAttend;
}
