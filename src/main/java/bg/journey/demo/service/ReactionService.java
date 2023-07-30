package bg.journey.demo.service;

import bg.journey.demo.dto.payload.ReactionCreationDTO;
import bg.journey.demo.exception.CommentNotFoundException;
import bg.journey.demo.exception.NotAuthorizedException;
import bg.journey.demo.exception.ReactionNotFoundException;
import bg.journey.demo.exception.RouteNotFoundException;
import bg.journey.demo.model.entity.CommentEntity;
import bg.journey.demo.model.entity.ReactionEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.model.enums.ReactionTargetType;
import bg.journey.demo.repository.CommentRepository;
import bg.journey.demo.repository.ReactionRepository;
import bg.journey.demo.repository.RouteRepository;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    public ReactionService(ReactionRepository reactionRepository,
                           CommentRepository commentRepository,
                           RouteRepository routeRepository,
                           UserRepository userRepository) {
        this.reactionRepository = reactionRepository;
        this.commentRepository = commentRepository;
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
    }


    public void react(Long entityId, UserPrincipal userPrincipal, ReactionCreationDTO reactionCreationDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        if (reactionCreationDTO.getReactionTargetType().equals(ReactionTargetType.ROUTE)) {

            RouteEntity routeEntity = routeRepository.findById(entityId)
                    .orElseThrow(RouteNotFoundException::new);

            ReactionEntity reactionEntity = reactionRepository.save(
                    ReactionEntity.builder()
                            .reactionType(reactionCreationDTO.getReactionType())
                            .author(userEntity)
                            .reactionTargetType(reactionCreationDTO.getReactionTargetType())
                            .targetEntityId(routeEntity.getId())
                            .build());
            routeEntity.getReactions().add(reactionEntity);
            routeRepository.save(routeEntity);

        } else if (reactionCreationDTO.getReactionTargetType().equals(ReactionTargetType.COMMENT)) {

            CommentEntity commentEntity = commentRepository.findById(entityId)
                    .orElseThrow(CommentNotFoundException::new);

            ReactionEntity reactionEntity = reactionRepository.save(
                    ReactionEntity.builder()
                            .reactionType(reactionCreationDTO.getReactionType())
                            .author(userEntity)
                            .reactionTargetType(reactionCreationDTO.getReactionTargetType())
                            .targetEntityId(commentEntity.getId())
                            .build());
            commentEntity.getReactions().add(reactionEntity);
            commentRepository.save(commentEntity);
        }
    }

    public void editReaction(Long entityId, UserPrincipal userPrincipal, ReactionCreationDTO reactionCreationDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        if (reactionCreationDTO.getReactionTargetType().equals(ReactionTargetType.ROUTE)) {

            RouteEntity routeEntity = routeRepository.findById(entityId)
                    .orElseThrow(RouteNotFoundException::new);

            ReactionEntity reactionEntity = reactionRepository
                    .findByAuthorAndTargetEntityId(userEntity, routeEntity.getId())
                    .orElseThrow(ReactionNotFoundException::new);

            reactionEntity.setReactionType(reactionCreationDTO.getReactionType());
            reactionRepository.save(reactionEntity);

        } else if (reactionCreationDTO.getReactionTargetType().equals(ReactionTargetType.COMMENT)) {

            CommentEntity commentEntity = commentRepository.findById(entityId)
                    .orElseThrow(CommentNotFoundException::new);

            ReactionEntity reactionEntity = reactionRepository
                    .findByAuthorAndTargetEntityId(userEntity, commentEntity.getId())
                    .orElseThrow(ReactionNotFoundException::new);

            reactionEntity.setReactionType(reactionCreationDTO.getReactionType());
            reactionRepository.save(reactionEntity);
        }
    }

    public void deleteReaction(Long entityId, UserPrincipal userPrincipal, ReactionTargetType targetType) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        ReactionEntity reactionEntity;
        if (targetType.equals(ReactionTargetType.ROUTE)) {
            RouteEntity routeEntity = routeRepository.findById(entityId)
                    .orElseThrow(RouteNotFoundException::new);

            reactionEntity = reactionRepository
                    .findByAuthorAndTargetEntityId(userEntity, routeEntity.getId())
                    .orElseThrow(ReactionNotFoundException::new);

            routeEntity.getReactions().removeIf(r -> r.getId().equals(reactionEntity.getId()));

            routeRepository.save(routeEntity);

        } else if (targetType.equals(ReactionTargetType.COMMENT)) {
            CommentEntity commentEntity = commentRepository.findById(entityId)
                    .orElseThrow(CommentNotFoundException::new);

            reactionEntity = reactionRepository
                    .findByAuthorAndTargetEntityId(userEntity, commentEntity.getId())
                    .orElseThrow(ReactionNotFoundException::new);

            commentEntity.getReactions().removeIf(r -> r.getId().equals(reactionEntity.getId()));

            commentRepository.save(commentEntity);

        } else {
            throw new BadCredentialsException("Invalid input arguments");
        }
        reactionRepository.delete(reactionEntity);

    }

}
