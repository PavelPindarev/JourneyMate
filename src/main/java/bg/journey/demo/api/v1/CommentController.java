package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.CreationCommentDTO;
import bg.journey.demo.dto.response.CommentDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bg.journey.demo.config.AppConstants.API_BASE;

@RestController
@RequestMapping(path = API_BASE + "/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/routes/{routeId}")
    public ResponseEntity<ResponseDTO<List<CommentDTO>>> getAllCommentsForARoute(@PathVariable(value = "routeId") Long routeId) {
        List<CommentDTO> allCommentsForRoute = commentService.getAllCommentsForRoute(routeId);

        return ResponseEntity.ok(
                ResponseDTO.<List<CommentDTO>>builder()
                        .content(allCommentsForRoute)
                        .status(HttpStatus.OK.value())
                        .message("Successfully get all comments for a route!")
                        .build()
        );
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<CommentDTO>> getCommentById(@PathVariable(value = "commentId") Long commentId) {
        CommentDTO commentDTO = commentService.getCommentById(commentId);

        return ResponseEntity.ok(
                ResponseDTO.<CommentDTO>builder()
                        .content(commentDTO)
                        .status(HttpStatus.OK.value())
                        .message("Successfully get a comment by id!")
                        .build()
        );
    }

    @PostMapping("/{routeId}")
    public ResponseEntity<ResponseDTO<Object>> createACommentForRoute(@PathVariable(value = "routeId") Long routeId,
                                                                      @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                      @Valid @RequestBody CreationCommentDTO creationCommentDTO,
                                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest()
                    .body(
                            ResponseDTO
                                    .builder()
                                    .status(HttpStatus.BAD_REQUEST.value())
                                    .message(String.join(", ", errors))
                                    .content(null)
                                    .build()
                    );
        }
        commentService.commentARoute(userPrincipal, routeId, creationCommentDTO);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .content(null)
                        .status(HttpStatus.OK.value())
                        .message("Successfully create a comment comment!")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtility.isCommentOwner(#commentId, principal)")
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<Object>> editComment(@PathVariable(value = "commentId") Long commentId,
                                                           @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @Valid @RequestBody CreationCommentDTO creationCommentDTO,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest()
                    .body(
                            ResponseDTO
                                    .builder()
                                    .status(HttpStatus.BAD_REQUEST.value())
                                    .message(String.join(", ", errors))
                                    .content(null)
                                    .build()
                    );
        }
        commentService.editAComment(commentId, creationCommentDTO);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully edit a comment!")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtility.isCommentOwner(#commentId, principal)")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDTO<Object>> deleteComment(@PathVariable(value = "commentId") Long commentId,
                                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully delete a comment!")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }


}