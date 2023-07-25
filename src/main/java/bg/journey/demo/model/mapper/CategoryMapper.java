package bg.journey.demo.model.mapper;

import bg.journey.demo.dto.response.CategoryDTO;
import bg.journey.demo.model.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO categoryEntityToCategoryDTO(CategoryEntity categoryEntity);
}
