package bg.journey.demo.exception;

import org.springframework.http.HttpStatus;

public class RouteNotFoundException extends ApiException {
    public RouteNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Route not found");
    }

    public RouteNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}