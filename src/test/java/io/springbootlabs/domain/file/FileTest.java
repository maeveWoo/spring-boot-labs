package io.springbootlabs.domain.file;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class FileTest {
    @Test
    public void textFileTypeCheck() {
        MockMultipartFile file = new MockMultipartFile(
                "test",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "hello".getBytes()
        );
        assertThat(Type.filterType(file)).isEqualTo(Type.Text);
        assertThat(Kind.valueOfName(file.getContentType()).getName()).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
    }

    @Test
    public void imageFileTypeCheck() throws IOException {
        ClassPathResource image10mbJpg = new ClassPathResource("file/10mb.jpeg");
        MockMultipartFile file = new MockMultipartFile(
                "test",
                "10mb.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                image10mbJpg.getInputStream()
        );
        assertThat(Type.filterType(file)).isEqualTo(Type.Image);
        assertThat(Kind.valueOfName(file.getContentType()).getName()).isEqualTo(MediaType.IMAGE_JPEG_VALUE);
    }

    @Test
    public void bmpFileTypeCheck() throws IOException {
        ClassPathResource image10mbJpg = new ClassPathResource("file/10mb.jpeg");
        MockMultipartFile file = new MockMultipartFile(
                "test",
                "10mb.jpeg",
                Kind.IMAGE_BMP_VALUE,
                image10mbJpg.getInputStream()
        );
        assertThat(Type.filterType(file)).isEqualTo(Type.Bmp);
        assertThat(Kind.valueOfName(file.getContentType()).getName()).isEqualTo(Kind.IMAGE_BMP_VALUE);
    }

    @Test
    public void mp3FileTypeCheck() throws IOException {
        // TODO sample mp3 줍줍
        ClassPathResource image10mbJpg = new ClassPathResource("file/10mb.jpeg");
        MockMultipartFile file = new MockMultipartFile(
                "test",
                "10mb.mp3",
                Kind.AUDEO_MP3_VALUE,
                image10mbJpg.getInputStream()
        );
        assertThat(Type.filterType(file)).isEqualTo(Type.Mp3);
        assertThat(Kind.valueOfName(file.getContentType()).getName()).isEqualTo(Kind.AUDEO_MP3_VALUE);
    }
}