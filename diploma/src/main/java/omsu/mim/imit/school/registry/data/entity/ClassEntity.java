package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "class")
@NoArgsConstructor
@AllArgsConstructor
public class ClassEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "theme")
    private String theme;

    @Column(name = "class_date_time")
    private LocalDateTime classDateTime;
}
