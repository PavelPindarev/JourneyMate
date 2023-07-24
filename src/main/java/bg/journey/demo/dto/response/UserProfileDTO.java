package bg.journey.demo.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private LocalDateTime createdOn;
    private PictureDTO profilePicture;
}