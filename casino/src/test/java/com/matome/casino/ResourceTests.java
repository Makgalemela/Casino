package com.matome.casino;


import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ResourceTests {

    @Autowired
    private MockMvc mockMvc;



    @Test
    public  void createNewPlayer() throws Exception {
        String json = "{\"name\": \"Matomea\"}";
        mockMvc.perform(
                post("/new/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }


    @Test
    public void depositIntoPlayerAccount() throws Exception {

        String json = "{\"name\": \"Matome\"}";
        mockMvc.perform(
                post("/new/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());


        String json1 = "{\"playerId\": 1, \"amount\" : 10000}";
        mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isCreated());
    }


    @Test
    public void getPlayerBalance() throws Exception {

        String json = "{\"name\": \"Matome\"}";
        mockMvc.perform(
                post("/new/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());


        String json1 = "{\"playerId\": 1, \"amount\" : 10000}";
        mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isCreated());


        String balance = "{\n" +
                "  \"data\": {\n" +
                "    \"balance\": \"10000.0\"\n" +
                "  },\n" +
                "  \"message\": \"Successful\",\n" +
                "  \"status\": 200,\n" +
                "  \"isSuccess\": true\n" +
                "}";

        mockMvc.perform(get("/balance?playerId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(balance));

    }


    @Test
    public void withdrawFromPlayerAccount() throws Exception {

        String json = "{\"name\": \"Matome\"}";
        mockMvc.perform(
                post("/new/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());


        String json1 = "{\"playerId\": 1, \"amount\" : 10000}";
        mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isCreated());



        String json2 = "{\"playerId\": 1, \"amount\" : 10000}";
        mockMvc.perform(
                post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isCreated());


        String balance = "{\n" +
                "  \"data\": {\n" +
                "    \"balance\": \"0.0\"\n" +
                "  },\n" +
                "  \"message\": \"Successful\",\n" +
                "  \"status\": 200,\n" +
                "  \"isSuccess\": true\n" +
                "}";

        mockMvc.perform(get("/balance?playerId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(balance));

    }


    @Test
    public void overWithDrawFromPlayerAccount() throws Exception {

        String json = "{\"name\": \"Matome\"}";
        mockMvc.perform(
                post("/new/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());


        String json1 = "{\"playerId\": 1, \"amount\" : 10000}";
        mockMvc.perform(
                post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isCreated());



        String json2 = "{\"playerId\": 1, \"amount\" : 1000000}";
        mockMvc.perform(
                post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isIAmATeapot());

    }
}
