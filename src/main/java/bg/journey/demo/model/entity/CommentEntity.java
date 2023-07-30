package bg.journey.demo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @NotNull
    @Length(max = 255)
    private String textContent;

    @ManyToOne
    private UserEntity author;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<ReactionEntity> reactions;

    @ManyToOne
    private RouteEntity route;
}
