package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.ReactionCreationDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.model.enums.ReactionTargetType;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.ReactionService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static bg.journey.demo.config.AppConstants.API_BASE;

@RestController
@RequestMapping(API_BASE + "/reactions")
public class ReactionController {
    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    //REACTIONS TO ROUTE
    @PostMapping("/routes/{routeId}")
    public ResponseEntity<ResponseDTO<Object>> reactToRoute(@PathVariable(value = "routeId") Long routeId,
                                                            @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @Valid @RequestBody ReactionCreationDTO reactionCreationDTO,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors() || reactionCreationDTO.getReactionTargetType()
                .equals(ReactionTargetType.COMMENT)) {
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
        reactionService.react(routeId, userPrincipal, reactionCreationDTO);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully react to a route.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

    @PatchMapping("/routes/{routeId}")
    public ResponseEntity<ResponseDTO<Object>> editReactionToRoute(@PathVariable(value = "routeId") Long routeId,
                                                                   @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                   @Valid @RequestBody ReactionCreationDTO reactionCreationDTO,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors() || reactionCreationDTO.getReactionTargetType()
                .equals(ReactionTargetType.COMMENT)) {
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
        reactionService.editReaction(routeId, userPrincipal, reactionCreationDTO);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully edit reaction to a route.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

    @DeleteMapping("/routes/{routeId}")
    public ResponseEntity<ResponseDTO<Object>> deleteAReactionToRoute(@PathVariable(value = "routeId") Long routeId,
                                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reactionService.deleteReaction(routeId, userPrincipal, ReactionTargetType.ROUTE);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully delete a reaction to a route.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

    //REACTIONS TO COMMENT
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDTO<Object>> reactToComment(@PathVariable(value = "commentId") Long commentId,
                                                              @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              @Valid @RequestBody ReactionCreationDTO reactionCreationDTO,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors() || reactionCreationDTO.getReactionTargetType()
                .equals(ReactionTargetType.ROUTE)) {
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
        reactionService.react(commentId, userPrincipal, reactionCreationDTO);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully react to a comment.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDTO<Object>> editReactionToComment(@PathVariable(value = "commentId") Long commentId,
                                                                     @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                     @Valid @RequestBody ReactionCreationDTO reactionCreationDTO,
                                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors() || reactionCreationDTO.getReactionTargetType()
                .equals(ReactionTargetType.ROUTE)) {
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
        reactionService.editReaction(commentId, userPrincipal, reactionCreationDTO);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully edit reaction to a comment.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDTO<Object>> deleteAReactionToComment(@PathVariable(value = "commentId") Long commentId,
                                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reactionService.deleteReaction(commentId, userPrincipal, ReactionTargetType.COMMENT);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully delete a reaction to a comment.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }
}
