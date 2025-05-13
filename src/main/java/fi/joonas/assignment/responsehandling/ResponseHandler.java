package fi.joonas.assignment.responsehandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity<ApiResponse> buildResponse(String message, HttpStatus status, double newBalance) {

        ApiResponse response = ApiResponse.builder()
                .message(message)
                .status(status.value())
                .newBalance(newBalance)
                .build();
        return new ResponseEntity<>(response, status);
    }

}
