package me.jarad.rates.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class ApiError implements Serializable  {


    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpStatus status;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timestamp;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @Getter
    @Setter
    @JsonIgnore
    private String logMessage;


    public ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.logMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String url, String message) {
        this();
        this.status = status;
        this.message = "Wrong url " + url;
        this.logMessage = message;
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.logMessage = ex.getLocalizedMessage();
    }


}