package bg.journey.demo.model.entity;

import bg.journey.demo.model.enums.ReactionEnum;
import bg.journey.demo.model.enums.ReactionTargetType;
import jakarta.persistence.*;
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

    @ManyToOne(optional = false)
    private UserEntity author;

    @Enumerated(EnumType.STRING)
    private ReactionTargetType reactionTargetType;
}
