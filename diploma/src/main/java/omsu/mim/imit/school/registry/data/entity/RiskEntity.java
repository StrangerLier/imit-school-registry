package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "risk")
public class RiskEntity extends BaseEntity {
    @Column(name = "reason")
    private String reason;

    @Column(name = "groupId")
    private UUID groupId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "supportId")
    private UUID supportId;
}
