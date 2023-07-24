package bg.journey.demo.api.v1;

import bg.journey.demo.dto.payload.SigninDTO;
import bg.journey.demo.dto.payload.SingupDTO;
import bg.journey.demo.dto.response.JwtResponseDTO;
import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.exception.InvalidCredentialsException;
import bg.journey.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.stream.Collectors;

import static bg.journey.demo.config.AppConstants.API_BASE;


@RestController
@RequestMapping(path = API_BASE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService userService) {
        this.authService = userService;
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<ResponseDTO<JwtResponseDTO>> signIn(@Valid @RequestBody SigninDTO signinDto) {

        try {
            String token = authService.signInUser(signinDto);
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(token, "Bearer");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            ResponseDTO
                                    .<JwtResponseDTO>builder()
                                    .message("Logged in successfully!")
                                    .content(jwtResponseDTO)
                                    .status(HttpStatus.OK.value())
                                    .build()
                    );
        } catch (InvalidCredentialsException ex) {
            return ResponseEntity
                    .status(ex.getStatus())
                    .body(
                            ResponseDTO
                                    .<JwtResponseDTO>builder()
                                    .message(ex.getMessage())
                                    .status(ex.getStatus().value())
                                    .build()
                    );
        }
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDTO<Object>> signUp(@Valid @RequestBody SingupDTO signupDto,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseDTO
                                    .builder()
                                    .message(String.join("; ", errorMessages))
                                    .status(HttpStatus.BAD_REQUEST.value())
                                    .build()
                    );
        }

        authService.signUpUser(signupDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Signed up successfully!")
                                .content(null)
                                .status(HttpStatus.CREATED.value())
                                .build()
                );

    }
}