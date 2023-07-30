package bg.journey.demo.dto.payload;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreationCommentDTO {

    @NotNull
    @Length(max = 255)
    private String textContext;
}
