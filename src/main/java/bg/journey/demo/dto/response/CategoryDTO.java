package bg.journey.demo.dto.response;

import bg.journey.demo.model.enums.CategoryType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;

    private CategoryType name;

    private String description;
}
