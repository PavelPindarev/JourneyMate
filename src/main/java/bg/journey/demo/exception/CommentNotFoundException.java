package bg.journey.demo.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends ApiException {

    public CommentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Comment not found");
    }

    public CommentNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
