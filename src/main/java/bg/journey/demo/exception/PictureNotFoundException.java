package bg.journey.demo.exception;

import org.springframework.http.HttpStatus;

public class PictureNotFoundException extends ApiException{

    public PictureNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Picture not found");
    }
    public PictureNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
