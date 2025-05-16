package omsu.mim.imit.school.registry.data.entity.xml;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "holiday")
@NoArgsConstructor
@AllArgsConstructor
public class HolidayEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "holiday")
    private LocalDate holiday;
}
