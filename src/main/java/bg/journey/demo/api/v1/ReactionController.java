package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.ReactionCreationDTO;
import bg.journey.demo.dto.response.ResponseDTO;
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

    @PostMapping("/routes/{routeId}")
    public ResponseEntity<ResponseDTO<Object>> reactToRoute(@PathVariable(value = "routeId") Long routeId,
                                                            @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @Valid @RequestBody ReactionCreationDTO reactionCreationDTO,
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
        reactionService.reactToRoute(routeId, userPrincipal, reactionCreationDTO);

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
        reactionService.editReactionToRoute(routeId, userPrincipal, reactionCreationDTO);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully edit reaction to a route.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

    @DeleteMapping("/routes/{routeId}")
    public ResponseEntity<ResponseDTO<Object>> deleteAReaction(@PathVariable(value = "routeId") Long routeId,
                                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        reactionService.deleteReactionToRoute(routeId, userPrincipal);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .message("Successfully delete a reaction to a route.")
                        .status(HttpStatus.OK.value())
                        .content(null)
                        .build()
        );
    }

}
