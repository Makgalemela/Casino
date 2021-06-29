package com.matome.casino;


import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class NoResourceTests {


    @Autowired
    private MockMvc mockMvc;


    @Test
    public  void depositMoneyForNonExistingPlayer() throws Exception {
        String json = "{\"playerId\": 1, \"amount\": 10000}";
        mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

    }

    @Test
    public  void getTransactionOfNonExistingPlayer() throws Exception {
        mockMvc.perform(get("/transaction?transitionId=11"))
                .andExpect(status().isNotFound());
    }

    @Test
    public  void withdrawWhenTheUserDoesNotExist() throws Exception {
        String json = "{\"playerId\": 1, \"amount\": 10000}";
        mockMvc.perform(
                post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    public  void getAllPlayersWhenThereIsNoPlayers() throws Exception {
        mockMvc.perform(get("/all/players"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public  void getAllTransactionsWhenThereIsNoTransactions() throws Exception {
        mockMvc.perform(get("/all/transactions"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public  void getLastTransactionsOfNonExistingPlayer() throws Exception {

        String json = "{\"password\": \"Casino$1234\", \"name\": \"Matome10\"}";
        mockMvc.perform(
                post("/last/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}
