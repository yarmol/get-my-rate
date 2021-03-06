package me.jarad.rates.handler;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;


@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({
            ConversionFailedException.class
    })
    public ResponseEntity<ApiError> registrationErrorHandler(Exception ex, HttpServletResponse response) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .message("Wrong incoming date format")
                .timestamp(LocalDateTime.now())
                .build();
        response.setStatus(apiError.getStatus().value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiError> paramsErrorHandler(Exception ex, HttpServletResponse response) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Not enough query parameters")
                .timestamp(LocalDateTime.now())
                .build();
        response.setStatus(apiError.getStatus().value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }


    @ExceptionHandler({
            WrongRequestException.class
    })
    public ResponseEntity<ApiError> badRequestHandler(Exception ex,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message( String.format("Bad request url (%s) or method ",requestURI))
                .timestamp(LocalDateTime.now())
                .build();
        response.setStatus(apiError.getStatus().value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
