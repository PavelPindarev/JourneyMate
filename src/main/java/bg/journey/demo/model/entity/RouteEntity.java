package bg.journey.demo.model.entity;

import bg.journey.demo.model.enums.LevelEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "routes")
public class RouteEntity extends BaseEntity {

    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String gpxCoordinates;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LevelEnum levelType;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String videoUrl;

    @OneToOne
    private PictureEntity mainPicture;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PictureEntity> pictures;

    @ManyToMany
    private Set<CategoryEntity> categories;

    @ManyToOne(optional = false)
    private UserEntity author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReactionEntity> reactions;



}
