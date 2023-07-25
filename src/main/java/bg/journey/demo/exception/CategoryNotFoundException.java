package bg.journey.demo.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends ApiException {

    public CategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Category not found");
    }
    public CategoryNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
