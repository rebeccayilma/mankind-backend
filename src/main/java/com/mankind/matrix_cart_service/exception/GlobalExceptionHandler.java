import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Unhandled exception", ex);
        return createErrorResponse(
                "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
} 