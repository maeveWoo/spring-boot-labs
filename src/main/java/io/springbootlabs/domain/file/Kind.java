package io.springbootlabs.domain.file;

import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
 */
@Getter
public enum Kind {
    TXT(MediaType.TEXT_PLAIN_VALUE),
    PNG(MediaType.IMAGE_PNG_VALUE),
    JPEG(MediaType.IMAGE_JPEG_VALUE),
    JPG(Kind.IMAGE_JPG_VALUE),
    BMP(Kind.IMAGE_BMP_VALUE),
    MP3(Kind.AUDEO_MP3_VALUE)
    ;
    private String name;

    public static final String IMAGE_JPG_VALUE = "image/jpg"; // 얘는 왜 없나? 표준이 아닌지 확인
    public static final String IMAGE_BMP_VALUE = "image/bmp"; // 얘는 왜 없나? 표준이 아닌지 확인
    public static final String AUDEO_MP3_VALUE = "audio/mpeg"; // 얘는 왜 없나? 표준이 아닌지 확인
    private static final Map<String, Kind> BY_NAME = new HashMap<>();

    static {
        for (Kind k: values()) {
            BY_NAME.put(k.getName(), k);
        }
    }

    Kind(String name) {
        this.name = name;
    }

    public static Kind valueOfName(String name) {
        return BY_NAME.get(name);
    }
}
