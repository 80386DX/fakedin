package net.etwas.fakedin.exception;

import net.etwas.fakedin.dto.AdCheckResponse;
import net.etwas.fakedin.dto.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidLinkedInUrlException.class)
    public ResponseEntity<AdCheckResponse> handleInvalidUrl(InvalidLinkedInUrlException ex) {
        AdCheckResponse response = new AdCheckResponse(
                ResponseStatus.ERROR,
                0
        );
        return ResponseEntity.badRequest().body(response);
    }

    // Ovde možeš dodavati i druge exception-e kasnije
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AdCheckResponse> handleGeneralException(Exception ex) {
        AdCheckResponse response = new AdCheckResponse(
                ResponseStatus.ERROR,
                0
        );
        return ResponseEntity.internalServerError().body(response);
    }
}