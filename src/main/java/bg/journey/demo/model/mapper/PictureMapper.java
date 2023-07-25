package bg.journey.demo.model.mapper;

import bg.journey.demo.dto.response.PictureDTO;
import bg.journey.demo.model.entity.PictureEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PictureMapper {

    PictureDTO pictureEntityToPictureDTO(PictureEntity pictureEntity);
}
