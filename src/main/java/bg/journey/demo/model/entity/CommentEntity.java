package bg.journey.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

   @OneToMany
   private Set<ReactionEntity> reactions;

   @ManyToOne
   private RouteEntity route;
}
