package bg.journey.demo.dto.response;

import bg.journey.demo.model.enums.LevelEnum;

import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDetailsViewDTO {

    private Long id;

    private String name;

    private LevelEnum levelType;

    private String gpxCoordinates;

    private String description;

    private PictureDTO mainPicture;

    private Set<CategoryDTO> categories;

    private String authorUsername;

    private Set<ReactionDTO> reactions;

    private Set<PictureDTO> pictures;

    private String videoUrl;
}
