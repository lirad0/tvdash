package com.tvdash.backend.controller;

import com.tvdash.backend.config.MinioProperties;
import com.tvdash.backend.model.TableauCard;
import com.tvdash.backend.repository.TableauCardRepository;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableauCardController.class)
class TableauCardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableauCardRepository repository;

    @MockBean
    private MinioClient minioClient;

    @MockBean
    private MinioProperties minioProperties;

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

    @Test
    void shouldUploadImageAndSaveTheGeneratedObjectName() throws Exception {
        TableauCard card = new TableauCard();
        card.setName("Sample");
        card.setImageUrl("generated-image.png");
        card.setUrl("https://example.com");

        when(minioProperties.getBucket()).thenReturn("images");
        doReturn(null).when(minioClient).putObject(any());
        when(repository.save(any(TableauCard.class))).thenReturn(card);

        mockMvc.perform(multipart("/api/tableau/cards/upload")
                        .file(new MockMultipartFile("file", "sample.png", MediaType.IMAGE_PNG_VALUE, "image-data".getBytes(StandardCharsets.UTF_8)))
                        .param("name", "Sample")
                        .param("url", "https://example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample"))
                .andExpect(jsonPath("$.imageUrl").isNotEmpty());
    }
}
