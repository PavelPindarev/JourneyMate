package bg.journey.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pictures")
public class PictureEntity extends BaseEntity {

    @NotBlank
    private String title;

    @NotBlank
    private String url;

}
