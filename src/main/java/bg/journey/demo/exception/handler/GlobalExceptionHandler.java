package bg.journey.demo.exception.handler;

import bg.journey.demo.dto.response.ResponseDTO;
import bg.journey.demo.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountExpiredException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<Object> handleApiExceptions(ApiException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(
                        ResponseDTO
                                .builder()
                                .status(exception.getStatus().value())
                                .message(exception.getMessage())
                                .content(null)
                                .build()
                );
    }

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class, LockedException.class,
            CredentialsExpiredException.class, AccountExpiredException.class})
    public ResponseEntity<Object> handleAccountExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(exception.getMessage())
                                .content(null)
                                .build()
                );
    }

    //for @PreAuthorize later
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(Exception exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ResponseDTO
                                .builder()
                                .status(HttpStatus.FORBIDDEN.value())
                                .message(exception.getMessage())
                                .content(null)
                                .build()
                );
    }

}
