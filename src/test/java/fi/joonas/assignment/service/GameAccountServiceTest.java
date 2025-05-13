package fi.joonas.assignment.service;

import fi.joonas.assignment.jpaentity.Customer;
import fi.joonas.assignment.jpaentity.GameEvent;
import fi.joonas.assignment.jsonentity.Event;
import fi.joonas.assignment.repository.CustomerRepository;
import fi.joonas.assignment.repository.GameEventRepository;
import fi.joonas.assignment.responsehandling.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static fi.joonas.assignment.jpaentity.GameEvent.newGameEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameAccountServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private GameEventRepository gameEventRepository;
    @InjectMocks
    private GameAccountService gameAccountService;

    static Customer customer_1 = new Customer(1L, "Joonas", 100.0);
    static Customer customer_2 = new Customer(2L, "Antti", 200.0);
    static Event eventCustomerNotExists = new Event(5L, 5L, 0.0);
    static Event eventTooBigAmount = new Event(1L, 1L, 110.0);
    static Event eventOk = new Event(1L, 1L, 20.0);
    static Event event_2 = new Event(1L, 2L, 20.0);
    static GameEvent gameEventOkPurchase = newGameEvent(eventOk, customer_1, GameEvent.TransactionType.PURCHASE, 80.0);
    static GameEvent gameEventAnotherCustomer = newGameEvent(event_2, customer_2, GameEvent.TransactionType.PURCHASE, 80.0);
    static GameEvent gameEventOkWin = newGameEvent(eventOk, customer_1, GameEvent.TransactionType.WIN, 80.0);

    @Test
    void testChargeCustomerNotExists() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.chargeGameAccount(eventCustomerNotExists);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testChargeAmountTooBig() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer_1));
        when(gameEventRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.chargeGameAccount(eventTooBigAmount);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testChargeNewEvent() {
        double expectedBalance = customer_1.getAccountBalance() - eventOk.getAmount();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer_1));
        when(gameEventRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.chargeGameAccount(eventOk);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedBalance, responseEntity.getBody().getNewBalance());
    }

    @Test
    void testChargeExistingEventOk() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer_1));
        when(gameEventRepository.findById(any())).thenReturn(Optional.of(gameEventOkPurchase));
        double originalBalance = customer_1.getAccountBalance();
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.chargeGameAccount(eventOk);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(gameEventOkPurchase.getOriginalBalance(), responseEntity.getBody().getNewBalance());
        assertEquals(originalBalance, customer_1.getAccountBalance());
    }

    @Test
    void testChargeExistingEventBelongsToAnotherCustomer() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer_1));
        when(gameEventRepository.findById(any())).thenReturn(Optional.of(gameEventAnotherCustomer));
        double originalBalance = customer_1.getAccountBalance();
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.chargeGameAccount(eventOk);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(originalBalance, customer_1.getAccountBalance());
    }

    @Test
    void testNotifyCustomerNotExists() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.notifyWin(eventCustomerNotExists);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testNotifyNewEvent() {
        double expectedBalance = customer_1.getAccountBalance() + eventOk.getAmount();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer_1));
        when(gameEventRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.notifyWin(eventOk);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedBalance, responseEntity.getBody().getNewBalance());
    }

    @Test
    void testNotifyExistingEventOk() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer_1));
        when(gameEventRepository.findById(any())).thenReturn(Optional.of(gameEventOkWin));
        double originalBalance = customer_1.getAccountBalance();
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.notifyWin(eventOk);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(gameEventOkWin.getOriginalBalance(), responseEntity.getBody().getNewBalance());
        assertEquals(originalBalance, customer_1.getAccountBalance());
    }

    @Test
    void testNotifyExistingEventBelongsToAnotherCustomer() {
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer_1));
        when(gameEventRepository.findById(any())).thenReturn(Optional.of(gameEventAnotherCustomer));
        double originalBalance = customer_1.getAccountBalance();
        ResponseEntity<ApiResponse> responseEntity = gameAccountService.notifyWin(eventOk);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(originalBalance, customer_1.getAccountBalance());
    }
}