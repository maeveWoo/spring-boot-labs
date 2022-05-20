package io.springbootlabs.domain.usecase;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorage {
    void store(MultipartFile file);
    Stream<Path> loadAll() throws FileNotFoundException;
    Path load(String filename);
    Resource loadAsResource(String filename);
    void deleteAll();
}
