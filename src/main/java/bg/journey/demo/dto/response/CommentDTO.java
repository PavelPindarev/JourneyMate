package bg.journey.demo.dto.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String textContent;
    private String authorUsername;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Set<ReactionDTO> reactions;

}
