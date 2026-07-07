package com.tvdash.backend.controller;

import com.tvdash.backend.model.TableauCard;
import com.tvdash.backend.repository.TableauCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableauCardController.class)
class TableauCardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableauCardRepository repository;

    @Test
    void shouldCreateCard() throws Exception {
        TableauCard card = new TableauCard();
        card.setName("Sample");
        card.setImageUrl("img/sample.png");
        card.setUrl("https://example.com");

        when(repository.save(any(TableauCard.class))).thenReturn(card);

        mockMvc.perform(post("/api/tableau/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sample\",\"imageUrl\":\"img/sample.png\",\"url\":\"https://example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample"));
    }
}
