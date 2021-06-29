package com.matome.casino.controller;


import com.matome.casino.dto.AdminAccess;
import com.matome.casino.dto.PlayerDto;
import com.matome.casino.dto.Transition;
import com.matome.casino.model.Player;
import com.matome.casino.service.CasinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class CasinoController {


    @Autowired
    private CasinoService casinoService;


    @GetMapping(value = "/balance")
    public ResponseEntity<Object> getBalance(@RequestParam Long playerId) {
        return casinoService.getBalance(playerId);
    }

    @PostMapping(value = "/deposit")
    public ResponseEntity<Object> deposit(@RequestBody Transition data) {
        return casinoService.deposit(data);
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity<Object> deduction(@RequestBody Transition data) {
        return casinoService.withdraw(data);
    }

    @GetMapping(value = "/transaction")
    public ResponseEntity<Object> getTransitionInfo(@RequestParam Long transitionId) {
        return casinoService.getTransitionInformation(transitionId);
    }


    @PostMapping(value = "/last/transactions")
    public ResponseEntity<Object> getLastTransactions(@RequestBody AdminAccess data) {
        return casinoService.getRecentTransaction(data);
    }

    @PostMapping(value = "/new/player")
    public ResponseEntity<Object> saveNewPlayer(@RequestBody PlayerDto player) {
        return casinoService.addNewPlayer(player);
    }

    @GetMapping(value = "/all/players")
    public ResponseEntity<Object> getAllPlayers() {
        return casinoService.getAllPlayers();
    }

    @GetMapping(value = "/all/transactions")
    public ResponseEntity<Object> getAllTransactions() {
        return casinoService.getAllTransaction();
    }

}
