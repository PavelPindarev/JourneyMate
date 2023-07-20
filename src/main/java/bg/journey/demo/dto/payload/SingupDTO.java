package bg.journey.demo.dto.payload;

import bg.journey.demo.utils.validators.BirthdayValidator;
import bg.journey.demo.utils.validators.EmailValidator;
import bg.journey.demo.utils.validators.UsernameValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@BirthdayValidator
public class SingupDTO {
    @UsernameValidator
    private String username;
    @EmailValidator
    private String email;
    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 25, message = "Password must be at least 6 symbols.")
    private String password;
    @NotBlank(message = "First name is required.")
    @Size(min = 3, max = 15, message = "Please enter First name between 3 and 15 symbols.")
    private String firstName;
    @NotBlank(message = "Last name is required.")
    @Size(min = 3, max = 15, message = "Please enter Last name between 3 and 15 symbols.")
    private String lastName;
    private LocalDate birthdate;
}