package fi.joonas.assignment.jpaentity;

import fi.joonas.assignment.jsonentity.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEvent {

    public enum TransactionType {
        PURCHASE,
        WIN
    }

    @Id
    private Long id;

    @NotNull
    @ManyToOne
    private Customer customer;

    @NotNull
    @Column(nullable = false)
    String eventType;

    @NotNull
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    @Column(nullable = false)
    double amount;

    @NotNull
    @Column(nullable = false)
    double originalBalance;

    public static GameEvent newGameEvent(Event event, Customer customer, TransactionType transactionType, double originalBalance) {
        GameEvent gameEvent = new GameEvent();
        gameEvent.setId(event.getGameEventId());
        gameEvent.setCustomer(customer);
        gameEvent.setEventType(transactionType.name());
        gameEvent.setCreatedAt(LocalDateTime.now());
        gameEvent.setAmount(event.getAmount());
        gameEvent.setOriginalBalance(originalBalance);
        return gameEvent;
    }
}
