package omsu.mim.imit.school.registry.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddContractInfoRequest {
    private UUID childId;

    private String conclusionDate;

    private String payerFullname;

    private Integer paymentAmount;

    private String paymentType;

    private Integer sale;

    private String direction;
}
