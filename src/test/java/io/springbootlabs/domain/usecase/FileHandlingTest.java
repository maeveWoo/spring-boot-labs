package io.springbootlabs.domain.usecase;

import io.springbootlabs.domain.file.File;
import io.springbootlabs.domain.file.FileRepository;
import io.springbootlabs.domain.file.Kind;
import io.springbootlabs.domain.file.Type;
import io.springbootlabs.domain.user.User;
import io.springbootlabs.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class FileHandlingTest {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private FileHandling fileHandling;

    @Test
    @DisplayName("사용자가 올린 이미지파일 전체조회")
    public void fileList() {
        User user = createUser();

        List<File> testList = new ArrayList();
        testList.add(File.createFile(user, Type.Image, "test2", Kind.PNG, 7700L));
        testList.add(File.createFile(user, Type.Image, "test1", Kind.PNG, 2000L));
        testList.add(File.createFile(user, Type.Image, "test3", Kind.PNG, 4000L));
        testList.add(File.createFile(user, Type.Image, "test4", Kind.PNG, 5100L));
        testList.add(File.createFile(user, Type.Image, "test5", Kind.PNG, 5500L));
        fileRepository.saveAll(testList);

        List<File> fileList = fileHandling.findListBy(user.getId());
        assertThat(fileList.size()).isEqualTo(5);
        assertThat(fileList).extracting("type").containsOnly(Type.Image);
    }

    @Test
    public void fileSave() throws IOException {
        User user = createUser();

        ClassPathResource image10mbJpg = new ClassPathResource("file/10mb.jpeg");
        String fileName = "10mb.jpeg";
        MockMultipartFile file = new MockMultipartFile(
                "10mb",
                fileName,
                MediaType.IMAGE_JPEG_VALUE,
                image10mbJpg.getInputStream()
        );

        Long saveFileId = fileHandling.save(user.getId(), file);
        Optional<File> savedFile = fileRepository.findById(saveFileId);

        assertThat(savedFile).isNotEmpty();
        assertThat(savedFile.get().getName()).isEqualTo(fileName);
        assertThat(savedFile.get().getKind()).isEqualTo(Kind.valueOfName(MediaType.IMAGE_JPEG_VALUE));
    }
    
    private User createUser() {
        User user = User.createUser("test", "test");
        return userRepository.save(user);
    } 
}