package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "contract")
public class ContractEntity extends BaseEntity {
@Column(name = "conclusion_date")

    private LocalDate conclusionDate;

    @Column(name = "payer_fullname")
    private String payerFullname;

    @Column(name = "child_id")
    private UUID childId;

    @Column(name = "payment_amount")
    private Integer paymentAmount;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "sale")
    private Integer sale;

    @Column(name = "direction")
    private String direction;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "file")
    private byte[] file;
}
