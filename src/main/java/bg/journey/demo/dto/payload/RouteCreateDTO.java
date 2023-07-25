package bg.journey.demo.dto.payload;


import bg.journey.demo.model.enums.LevelEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteCreateDTO {

    @Length(min = 5, max = 70, message = "Name is required and should be between 5 and 70 characters")
    private String name;

    @NotNull
    private LevelEnum levelType;

    @NotNull
    @Length(min = 6, message = "gpxCoordinates are required and should be more than 6 characters")
    private String gpxCoordinates;

    private String description;

    @NotEmpty
    private Set<Long> categoriesId;

    private String videoUrl;
}
