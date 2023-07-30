package bg.journey.demo.security;

import bg.journey.demo.exception.CommentNotFoundException;
import bg.journey.demo.exception.NotAuthorizedException;
import bg.journey.demo.exception.RouteNotFoundException;
import bg.journey.demo.model.entity.CommentEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.repository.CommentRepository;
import bg.journey.demo.repository.RouteRepository;
import bg.journey.demo.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class SecurityExpressionUtility {

    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final CommentRepository commentRepository;

    public SecurityExpressionUtility(UserRepository userRepository, RouteRepository routeRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.routeRepository = routeRepository;
        this.commentRepository = commentRepository;
    }

    public boolean isRouteOwner(Long resourceId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);

        RouteEntity routeEntity = routeRepository.findById(resourceId)
                .orElseThrow(RouteNotFoundException::new);

        return routeEntity.getAuthor().getId().equals(loggedUser.getId());
    }

    public boolean isCommentOwner(Long resourceId, UserPrincipal userPrincipal) {
        UserEntity loggedUser = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        CommentEntity commentEntity = commentRepository.findById(resourceId)
                .orElseThrow(CommentNotFoundException::new);

        return commentEntity.getAuthor().getId().equals(loggedUser.getId());
    }

}
