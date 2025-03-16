package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name = "group_info")
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "class_number")
    private String classNumber;

    @Column(name = "direction")
    private String direction;

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
}
