package bg.journey.demo.model.mapper;

import bg.journey.demo.dto.payload.CreationCommentDTO;
import bg.journey.demo.dto.response.CommentDTO;
import bg.journey.demo.model.entity.CommentEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    private final ReactionMapper reactionMapper = Mappers.getMapper(ReactionMapper.class);

    public CommentDTO commentEntityToCommentDTO(CommentEntity commentEntity) {

        return CommentDTO.builder()
                .id(commentEntity.getId())
                .authorUsername(commentEntity.getAuthor().getUsername())
                .textContent(commentEntity.getTextContent())
                .createdOn(commentEntity.getCreatedOn())
                .updatedOn(commentEntity.getUpdatedOn())
                .reactions(commentEntity.getReactions()
                        .stream()
                        .map(reactionMapper::reactionEntityToReactionDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    public CommentEntity commentDTOToCommentEntity(CreationCommentDTO creationCommentDTO, UserEntity userEntity, RouteEntity routeEntity) {
        return CommentEntity.builder()
                .route(routeEntity)
                .author(userEntity)
                .textContent(creationCommentDTO.getTextContext())
                .build();
    }
}
