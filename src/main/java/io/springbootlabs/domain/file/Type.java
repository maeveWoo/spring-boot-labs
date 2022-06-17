package io.springbootlabs.domain.file;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.MissingFormatArgumentException;

@Getter
public enum Type {
    Image, // image/jpeg, image/png
    Bmp, // image/bmp
    Mp3,
    Text;

    public static Type filterType(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        assert contentType != null;
        String[] split = contentType.split("/");

        if (split.length > 1) {
            if (split[0].equals(Type.Text.name().toLowerCase())) {
                return Type.Text;
            }
            if (split[1].equals(Type.Bmp.name().toLowerCase())) {
                return Type.Bmp;
            }
            if (split[0].equals(Type.Image.name().toLowerCase())){
                return Type.Image;
            }
            return Type.Mp3;
        }

        throw new MissingFormatArgumentException(contentType);
    }
}
