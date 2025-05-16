package omsu.mim.imit.school.registry.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "child")
public class ChildEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "address")
    private String address;

    @Column
    private String school;

    @Column(name = "class_number")
    private String classNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private ChildStatus status = ChildStatus.NEW;

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "parent_phone")
    private String parentPhone;

    @Column(name = "parent")
    private String parent;

    @Column(name = "skills")
    private String skills;

    @Column(name = "duplicate_key")
    private String duplicateKey;
}
