package fi.joonas.assignment.jsonentity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {
    @NotNull
    Long customerId;
    @NotNull
    Long gameEventId;
    @NotNull
    Double amount;
}
