package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.UpdateUserProfileDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.dto.response.UserProfileDTO;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.UserService;
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
@RequestMapping(path = API_BASE + "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ResponseDTO<UserProfileDTO>> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserProfileDTO myProfile = userService.getMyProfile(userPrincipal);

        return ResponseEntity.ok(
                ResponseDTO
                        .<UserProfileDTO>builder()
                        .content(myProfile)
                        .status(HttpStatus.OK.value())
                        .message("Successfully get your profile info!")
                        .build()
        );
    }

    //TODO: @PreAuthorize
    @PatchMapping("/my-profile")
    public ResponseEntity<ResponseDTO<Object>> updateMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                               @Valid @RequestBody UpdateUserProfileDTO updateUserProfileDTO,
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

        userService.updateMyProfile(userPrincipal, updateUserProfileDTO);
        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .message("Profile updated successfully!")
                                .content(null)
                                .build()
                );
    }

    @GetMapping(value = "/user-profile/{userId}")
    public ResponseEntity<ResponseDTO<UserProfileDTO>> getUserProfile(@PathVariable(value = "userId") Long userId) {
        UserProfileDTO userProfileDTO = userService.getUserProfile(userId);

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .<UserProfileDTO>builder()
                                .content(userProfileDTO)
                                .status(HttpStatus.OK.value())
                                .message("Successfully get user's profile info!")
                                .build()
                );

    }
}
