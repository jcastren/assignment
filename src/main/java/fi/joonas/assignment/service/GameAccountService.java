package fi.joonas.assignment.service;

import fi.joonas.assignment.jpaentity.Customer;
import fi.joonas.assignment.jpaentity.GameEvent;
import fi.joonas.assignment.jsonentity.Event;
import fi.joonas.assignment.repository.CustomerRepository;
import fi.joonas.assignment.repository.GameEventRepository;
import fi.joonas.assignment.responsehandling.ApiResponse;
import fi.joonas.assignment.responsehandling.ResponseHandler;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static fi.joonas.assignment.jpaentity.GameEvent.newGameEvent;

@Service
public class GameAccountService {

    private final CustomerRepository customerRepository;
    private final GameEventRepository gameEventRepository;

    public GameAccountService(CustomerRepository customerRepository, GameEventRepository gameEventRepository) {
        this.customerRepository = customerRepository;
        this.gameEventRepository = gameEventRepository;
    }

    public ResponseEntity<ApiResponse> chargeGameAccount(@NotNull Event event) {
        double eventRecordBalanceValue;
        Optional<Customer> optCustomer = customerRepository.findById(event.getCustomerId());
        if (optCustomer.isEmpty()) {
            return ResponseHandler.buildResponse("Customer with id %s not found".formatted(event.getCustomerId()), HttpStatus.NOT_FOUND, 0.0);
        }
        Customer customer = optCustomer.get();

        Optional<GameEvent> optGameEvent = gameEventRepository.findById(event.getGameEventId());
        if (optGameEvent.isEmpty()) {
            if (customer.getAccountBalance() <= event.getAmount()) {
                return ResponseHandler.buildResponse("Account balance is too low", HttpStatus.BAD_REQUEST, 0.0);
            }
            eventRecordBalanceValue = customer.getAccountBalance() - event.getAmount();
            customer.setAccountBalance(eventRecordBalanceValue);
            customerRepository.save(customer);
            gameEventRepository.save(newGameEvent(event, customer, GameEvent.TransactionType.PURCHASE, eventRecordBalanceValue));
        } else {
            GameEvent gameEvent = optGameEvent.get();
            if (!gameEvent.getCustomer().getId().equals(customer.getId())) {
                return ResponseHandler.buildResponse("Event with id %s found but belongs to another user".formatted(event.getGameEventId()), HttpStatus.BAD_REQUEST, 0.0);
            }
            if (!gameEvent.getEventType().equals(GameEvent.TransactionType.PURCHASE.name())) {
                return ResponseHandler.buildResponse("Event with id %s found but doesn't match transaction type".formatted(event.getGameEventId()), HttpStatus.BAD_REQUEST, 0.0);
            }
            eventRecordBalanceValue = gameEvent.getOriginalBalance();
        }
        return ResponseHandler.buildResponse("New game purchased", HttpStatus.OK, eventRecordBalanceValue);
    }

    public ResponseEntity<ApiResponse> notifyWin(@NotNull Event event) {
        double eventRecordBalanceValue;
        Optional<Customer> optCustomer = customerRepository.findById(event.getCustomerId());
        if (optCustomer.isEmpty()) {
            return ResponseHandler.buildResponse("Customer with id %s not found".formatted(event.getCustomerId()), HttpStatus.NOT_FOUND, 0.0);
        }
        Customer customer = optCustomer.get();

        Optional<GameEvent> optGameEvent = gameEventRepository.findById(event.getGameEventId());
        if (optGameEvent.isEmpty()) {
            eventRecordBalanceValue = customer.getAccountBalance() + event.getAmount();
            customer.setAccountBalance(eventRecordBalanceValue);
            customerRepository.save(customer);
            gameEventRepository.save(newGameEvent(event, customer, GameEvent.TransactionType.WIN, eventRecordBalanceValue));
        } else {
            GameEvent gameEvent = optGameEvent.get();
            if (!gameEvent.getCustomer().getId().equals(customer.getId())) {
                return ResponseHandler.buildResponse("Event with id %s found but belongs to another user".formatted(event.getGameEventId()), HttpStatus.BAD_REQUEST, 0.0);
            }
            if (!gameEvent.getEventType().equals(GameEvent.TransactionType.WIN.name())) {
                return ResponseHandler.buildResponse("Event with id %s found but doesn't match transaction type".formatted(event.getGameEventId()), HttpStatus.BAD_REQUEST, 0.0);
            }
            eventRecordBalanceValue = gameEvent.getOriginalBalance();
        }
        return ResponseHandler.buildResponse("Win notified to customer account", HttpStatus.OK, eventRecordBalanceValue);
    }
}
