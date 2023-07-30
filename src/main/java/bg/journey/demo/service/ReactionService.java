package bg.journey.demo.service;

import bg.journey.demo.dto.payload.ReactionCreationDTO;
import bg.journey.demo.exception.NotAuthorizedException;
import bg.journey.demo.exception.ReactionNotFoundException;
import bg.journey.demo.exception.RouteNotFoundException;
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
import org.springframework.stereotype.Service;

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


    public void reactToRoute(Long routeId, UserPrincipal userPrincipal, ReactionCreationDTO reactionCreationDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(RouteNotFoundException::new);


        ReactionEntity reactionEntity = reactionRepository.save(
                ReactionEntity.builder()
                        .reactionType(reactionCreationDTO.getReactionType())
                        .author(userEntity)
                        .reactionTargetType(ReactionTargetType.ROUTE)
                        .targetEntityId(routeEntity.getId())
                        .build());

        routeEntity.getReactions().add(reactionEntity);
    }

    public void editReactionToRoute(Long routeId, UserPrincipal userPrincipal, ReactionCreationDTO reactionCreationDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(RouteNotFoundException::new);

        ReactionEntity reactionEntity = reactionRepository
                .findByAuthorAndTargetEntityId(userEntity, routeEntity.getId())
                .orElseThrow(ReactionNotFoundException::new);
        reactionEntity.setReactionType(reactionCreationDTO.getReactionType());
        reactionRepository.save(reactionEntity);
    }

    public void deleteReactionToRoute(Long routeId, UserPrincipal userPrincipal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(RouteNotFoundException::new);

        ReactionEntity reactionEntity = reactionRepository
                .findByAuthorAndTargetEntityId(userEntity, routeEntity.getId())
                .orElseThrow(ReactionNotFoundException::new);

        routeEntity.getReactions().remove(reactionEntity);
        routeRepository.save(routeEntity);

        reactionRepository.delete(reactionEntity);

    }
}
