package fi.joonas.assignment.controller;

import fi.joonas.assignment.jsonentity.Event;
import fi.joonas.assignment.responsehandling.ApiResponse;
import fi.joonas.assignment.service.GameAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game-account")
public class GameAccountController {

    private final GameAccountService gameAccountService;

    public GameAccountController(GameAccountService gameAccountService) {
        this.gameAccountService = gameAccountService;
    }

    @PutMapping("/charge")
    public ResponseEntity<ApiResponse> chargeGameAccount(@RequestBody Event event) {
        return gameAccountService.chargeGameAccount(event);
    }

    @PutMapping("/notify-win")
    public ResponseEntity<ApiResponse> notifyWin(@RequestBody Event event) {
        return gameAccountService.notifyWin(event);
    }
}