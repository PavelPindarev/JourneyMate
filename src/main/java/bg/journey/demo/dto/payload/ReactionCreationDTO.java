package bg.journey.demo.dto.payload;

import bg.journey.demo.model.enums.ReactionEnum;
import bg.journey.demo.model.enums.ReactionTargetType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionCreationDTO {

    @NotNull
    private ReactionEnum reactionType;

    @NotNull
    private ReactionTargetType reactionTargetType;

}
