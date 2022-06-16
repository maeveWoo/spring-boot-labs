package io.springbootlabs.domain.usecase;

import io.springbootlabs.domain.file.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileHandling {
    public List<File> findListByUser(Long userId);

    public File findBy(Long fileId);

    public Long save(String fileName, MultipartFile multipartFile);
}
