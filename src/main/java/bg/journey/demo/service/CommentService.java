package bg.journey.demo.service;

import bg.journey.demo.dto.payload.CreationCommentDTO;
import bg.journey.demo.dto.response.CommentDTO;
import bg.journey.demo.exception.CommentNotFoundException;
import bg.journey.demo.exception.NotAuthorizedException;
import bg.journey.demo.exception.RouteNotFoundException;
import bg.journey.demo.model.entity.CommentEntity;
import bg.journey.demo.model.entity.ReactionEntity;
import bg.journey.demo.model.entity.RouteEntity;
import bg.journey.demo.model.entity.UserEntity;
import bg.journey.demo.model.mapper.CommentMapper;
import bg.journey.demo.repository.CommentRepository;
import bg.journey.demo.repository.ReactionRepository;
import bg.journey.demo.repository.RouteRepository;
import bg.journey.demo.repository.UserRepository;
import bg.journey.demo.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final RouteRepository routeRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    public CommentService(CommentRepository commentRepository,
                          RouteRepository routeRepository,
                          CommentMapper commentMapper,
                          UserRepository userRepository,
                          ReactionRepository reactionRepository) {
        this.commentRepository = commentRepository;
        this.routeRepository = routeRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.reactionRepository = reactionRepository;
    }

    public List<CommentDTO> getAllCommentsForRoute(Long routeId) {
        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(RouteNotFoundException::new);

        List<CommentEntity> commentsByRoute = commentRepository.findAllByRoute(routeEntity);

        return commentsByRoute
                .stream()
                .map(commentMapper::commentEntityToCommentDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO getCommentById(Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        return commentMapper.commentEntityToCommentDTO(commentEntity);
    }

    public void commentARoute(UserPrincipal userPrincipal, Long routeId, CreationCommentDTO creationCommentDTO) {
        UserEntity userEntity = userRepository
                .findByUsernameOrEmail(userPrincipal.getUsername(), userPrincipal.getUsername())
                .orElseThrow(NotAuthorizedException::new);

        RouteEntity routeEntity = routeRepository.findById(routeId)
                .orElseThrow(RouteNotFoundException::new);

        CommentEntity commentEntity = commentMapper
                .commentDTOToCommentEntity(creationCommentDTO, userEntity, routeEntity);

        commentRepository.save(commentEntity);
    }

    public void editAComment(Long commentId, CreationCommentDTO creationCommentDTO) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        commentEntity.setTextContent(creationCommentDTO.getTextContext());
        commentEntity.setUpdatedOn(LocalDateTime.now());

        commentRepository.save(commentEntity);
    }

    public void deleteComment(Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        Set<ReactionEntity> reactions = commentEntity.getReactions();
        commentEntity.setReactions(null);
        commentEntity.setAuthor(null);
        commentEntity.setRoute(null);
        reactionRepository.deleteAll(reactions);
        commentRepository.delete(commentEntity);
    }

}
