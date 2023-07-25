package bg.journey.demo.model.mapper;

import bg.journey.demo.dto.response.ReactionDTO;
import bg.journey.demo.model.entity.ReactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    @Mapping(source = "author.username", target = "authorUsername")
   ReactionDTO reactionEntityToReactionDTO(ReactionEntity reactionEntity);
}
