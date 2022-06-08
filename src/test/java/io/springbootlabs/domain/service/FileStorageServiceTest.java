package io.springbootlabs.domain.service;

import io.springbootlabs.domain.exception.StorageFileNotFoundException;
import io.springbootlabs.domain.usecase.FileStorage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FileStorageServiceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileStorage fileStorage;

//    @Test
//    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    public void listAllFiles() throws Exception {
        given(fileStorage.loadAll())
                .willReturn(Stream.of(Paths.get("first.txt")));
        mvc.perform(get("/files/")).andExpect(status().isOk())
                .andExpect(
                        model()
                                .attribute("files",
                                        Matchers.contains("http://localhost/files/first.txt", "http://localhost/files/second.txt")
                                )
                );
    }

//    @Test
//    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    public void saveUploadedFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "text.txt", "text/plain", "Spring Framework".getBytes());
        mvc.perform(multipart("/files/").file(multipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/"));

        then(fileStorage).should().store(multipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    public void WhenMissingFile404() throws Exception {
        given(fileStorage.loadAsResource("test.txt"))
                .willThrow(StorageFileNotFoundException.class);
        mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
    }
}