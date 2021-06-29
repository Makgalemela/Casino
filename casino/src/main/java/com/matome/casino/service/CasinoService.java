package com.matome.casino.service;


import com.matome.casino.dto.AdminAccess;
import com.matome.casino.dto.PlayerDto;
import com.matome.casino.dto.Transition;
import com.matome.casino.model.Player;
import com.matome.casino.model.Transaction;
import com.matome.casino.repository.PlayerRepository;
import com.matome.casino.repository.TransactionRepository;
import com.matome.casino.utils.ResponseHandler;
import org.hibernate.cache.internal.NoCachingTransactionSynchronizationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.tree.TreeNode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class CasinoService {


    /**
     * For simplicity I will place password as static variable.
     * Ideally I would need to expose other api to allow the admin to create password
     * As well created password encryption.
     */

    private static String Password = "Casino$1234";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public ResponseEntity<Object> getBalance(Long playerId) {

        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isPresent()) {
            Double balance = computerBalance(player.get());
            Map<String, String> transaction = new HashMap<String, String>() {{
                put("balance", String.valueOf(balance));
            }};

            return ResponseHandler.generateResponse(HttpStatus.OK, true, "Successful", transaction);

        } else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Unsuccessful");
        }
    }


    public ResponseEntity<Object> withdraw(Transition data) {

        Optional<Player> player = playerRepository.findById(data.getPlayerId());
        if (player.isPresent()) {
            Double balance = computerBalance(player.get());
            if (balance < data.getAmount()) {
                Map<String, String> transaction = new HashMap<String, String>() {{
                    put("message", "Insufficient fund");
                }};
                return ResponseHandler.generateResponse(HttpStatus.I_AM_A_TEAPOT, false, "Unsuccessful", transaction);
            } else {

                Transaction transaction = new Transaction();
                data.setAmount(data.getAmount() * -1);
                transaction.setAmount(data.getAmount());
                transaction.setPlayer(player.get());
                transactionRepository.save(transaction);
                return ResponseHandler.generateResponse(HttpStatus.CREATED, true, "successful");
            }

        } else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Unsuccessful");
        }
    }


    public ResponseEntity<Object> deposit(Transition data) {

        Optional<Player> player = playerRepository.findById(data.getPlayerId());
        if (player.isPresent() && data.getAmount() > 0.0) {
            Transaction transaction = new Transaction();
            transaction.setAmount(data.getAmount());
            transaction.setPlayer(player.get());
            transactionRepository.save(transaction);
            return ResponseHandler.generateResponse(HttpStatus.CREATED, true, "successful");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Unsuccessful");
        }
    }


    private Double computerBalance(Player player) {
        List<Transaction> transactionList = transactionRepository.findAllByPlayer(player);
        Double balance = transactionList.stream().flatMapToDouble(b -> DoubleStream.of(b.getAmount())).sum();
        return balance;
    }


    public ResponseEntity<Object> getTransitionInformation(Long transitionId) {

        Optional<Transaction> transaction = transactionRepository.findById(transitionId);

        if (transaction.isPresent()) {
            Map<String, Float> transactionData = new HashMap<String, Float>() {{
                put("amount", transaction.get().getAmount());
            }};
            return ResponseHandler.generateResponse(HttpStatus.I_AM_A_TEAPOT, false, "Unsuccessful", transactionData);

        } else {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, false, "Unsuccessful");
        }

    }

    public ResponseEntity<Object> getRecentTransaction(AdminAccess data) {
        Optional<Player> player = playerRepository.findByName(data.getName());
        if (!data.getPassword().equalsIgnoreCase(Password)) {
            Map<String, String> message = new HashMap<String, String>() {{
                put("message", "You are not authorised");
            }};
            return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, false, "Successful", message);
        } else if (player.isPresent()) {
            List<Transaction> transactions = transactionRepository.findAllByPlayer(player.get());
            if (transactions.size() > 10) {
                transactions = transactions.stream().skip(transactions.size() - 10).collect(Collectors.toList());
            }
            return ResponseHandler.generateResponse(HttpStatus.OK, true, "Successful", transactions);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Unsuccessful");
        }
    }

    public ResponseEntity<Object> addNewPlayer(PlayerDto player){
        Optional<Player> player1 = playerRepository.findByName(player.getName());
        if(player1.isPresent()){
            Map<String, String> message = new HashMap<String, String>() {{
                put("message", "A player with the same name already exits");
            }};
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Unsuccessful", message);
        }else{
            Player newPlayer =  new Player();
            newPlayer.setName(player.getName());
            playerRepository.save(newPlayer);
            return ResponseHandler.generateResponse(HttpStatus.CREATED, true, "Successful");
        }
    }

    public  ResponseEntity<Object> getAllPlayers(){
        List<Player> players = playerRepository.findAll();
        if(!players.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, true, "Successful", players);

        }else{
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Unsuccessful");

        }

    }


    public  ResponseEntity<Object>  getAllTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        if(!transactions.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, true, "Successful", transactions);

        }else{
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, false, "Unsuccessful");

        }
    }
}
