package bg.journey.demo.model.entity;

import bg.journey.demo.model.enums.CategoryType;
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
@Table(name = "categories")
public class CategoryEntity extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private CategoryType name;

    private String description;
}
