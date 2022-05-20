package io.springbootlabs.domain.service;

import io.springbootlabs.domain.usecase.FileStorage;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

@Service
public class LocalFileStorage implements FileStorage {

    @Override
    public void store(MultipartFile file) {

    }

    @Override
    public Stream<Path> loadAll() throws FileNotFoundException {
        File directory = new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX.concat("file")).getPath());
        File[] files = directory.listFiles();
        return Arrays.stream(files).map(file -> {
                    return Paths.get(file.getName());
                }
        );
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {

        return null;
    }

    @Override
    public void deleteAll() {

    }
}
