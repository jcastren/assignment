package fi.joonas.assignment.responsehandling;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse {
    private String message;
    private int status;
    private double newBalance;
}
