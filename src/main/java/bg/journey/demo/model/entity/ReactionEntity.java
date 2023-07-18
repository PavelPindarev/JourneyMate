package bg.journey.demo.model.entity;

import bg.journey.demo.model.enums.ReactionEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reactions")
public class ReactionEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ReactionEnum reactionType;

    //user, routes, comments
}
