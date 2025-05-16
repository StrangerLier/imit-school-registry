package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "class")
public class ClassEntity extends BaseEntity {
    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "theme")
    private String theme;

    @Column(name = "class_date_time")
    private LocalDateTime classDateTime;
}
