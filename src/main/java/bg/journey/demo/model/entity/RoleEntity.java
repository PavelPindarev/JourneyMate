package bg.journey.demo.model.entity;

import bg.journey.demo.model.enums.RoleEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
