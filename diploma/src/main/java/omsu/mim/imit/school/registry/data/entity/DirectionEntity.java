package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "direction")
public class DirectionEntity extends BaseEntity {
    @Column(name = "name")
    private String name;
}
