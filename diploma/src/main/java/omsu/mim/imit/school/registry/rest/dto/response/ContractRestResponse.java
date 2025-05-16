package omsu.mim.imit.school.registry.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRestResponse {
    private UUID id;
    private LocalDate conclusionDate;
    private String payerFullname;
    private UUID childId;
    private Integer paymentAmount;
    private String paymentType;
    private Integer sale;
    private String direction;
}
