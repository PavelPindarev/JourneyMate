package bg.journey.demo.model.entity;

import bg.journey.demo.model.enums.LevelEnum;
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
@Table(name = "routes")
public class RouteEntity extends BaseEntity {

    private String name;

    private String gpxCoordinates;

    @Enumerated(EnumType.STRING)
    private LevelEnum levelType;

    private String description;

    private String videoUrl;

    //pictures and categories
}
