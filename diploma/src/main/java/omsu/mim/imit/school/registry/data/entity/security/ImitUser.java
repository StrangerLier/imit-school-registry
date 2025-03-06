package omsu.mim.imit.school.registry.data.entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImitUser {

    @Id
    private UUID id;
    private String username;
    private String password;
}