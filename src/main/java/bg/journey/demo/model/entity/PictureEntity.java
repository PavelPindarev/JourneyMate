package bg.journey.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pictures")
public class PictureEntity extends BaseEntity {

    private String title;

    private String url;

}
