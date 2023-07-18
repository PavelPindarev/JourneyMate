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
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

//    private boolean approved;

    private String textContent;

//author, reactions, route
}
