package bg.journey.demo.dto.response;


import bg.journey.demo.model.enums.ReactionEnum;
import bg.journey.demo.model.enums.ReactionTargetType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionDTO {
    private ReactionEnum reactionType;

    private String authorUsername;

    private ReactionTargetType reactionTargetType;
}
