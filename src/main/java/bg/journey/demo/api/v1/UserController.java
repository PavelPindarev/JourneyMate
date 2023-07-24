package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.PictureUploadPayloadDTO;
import bg.journey.demo.dto.payload.UpdateUserProfileDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.dto.response.UserProfileDTO;
import bg.journey.demo.security.UserPrincipal;
import bg.journey.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> setUserProfileImage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                   @RequestParam(required = false) String title,
                                                                   @RequestPart MultipartFile file) {
        userService.setProfilePicture(userPrincipal, PictureUploadPayloadDTO
                .builder()
                .multipartFile(file)
                .title(title)
                .build()
        );

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .content(null)
                                .message("Profile Picture set successfully!")
                                .build()

                );
    }

    @DeleteMapping(value = "/profile-picture")
    public ResponseEntity<ResponseDTO<Object>> deleteUserProfileImage(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteProfileImage(userPrincipal);

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.OK.value())
                                .content(null)
                                .message("Profile Picture deleted successfully!")
                                .build()

                );
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user/{userId}/ban")
    public ResponseEntity<ResponseDTO<Object>> banUserFromApp(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        userService.banUserFromApp(principal, userId);

        return ResponseEntity
                .ok(
                        ResponseDTO
                                .builder()
                                .content(null)
                                .status(HttpStatus.OK.value())
                                .message("User banned from the app successfully!")
                                .build()
                );
    }
//DELETE profile maybe later

}
