package bg.journey.demo.exception;

import org.springframework.http.HttpStatus;

public class ReactionNotFoundException extends ApiException{

    public ReactionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Reaction not found");
    }

    public ReactionNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
