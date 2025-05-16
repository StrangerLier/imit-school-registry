package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "group_info")
public class GroupEntity extends BaseEntity {
    @Column(name = "class_number")
    private String classNumber;

    @Column(name = "direction_id")
    private UUID directionId;

    @Column(name = "address")
    private String address;

    @Column(name = "listeners_amount")
    private Integer listenersAmount;

    @Column(name = "approved_listeners")
    private Integer approvedListeners;

    @Column(name = "time")
    private String time;

    @Column(name = "teacher_id")
    private UUID teacherId;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "assistants_ids")
    private String assistantsIds;

    @Column(name = "is_risky")
    private Boolean isRisky;

    @Column(name = "status")
    private String status;
}
