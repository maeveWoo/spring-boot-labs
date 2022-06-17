package io.springbootlabs.domain.file.service;

import io.springbootlabs.domain.file.File;
import io.springbootlabs.domain.file.FileRepository;
import io.springbootlabs.domain.file.Kind;
import io.springbootlabs.domain.file.Type;
import io.springbootlabs.domain.usecase.FileHandling;
import io.springbootlabs.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TestInmemoryFileService implements FileHandling {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Override
    public List<File> findListBy(Long userId) {
        return fileRepository.findBy(userRepository.findById(userId).get(), Type.Image);
    }

    @Override
    public File findBy(Long fileId) {
        return null;
    }

    @Override
    public Long save(Long userId, MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Type type = Type.filterType(multipartFile); //multipartFile.part.fileItem.fieldName
        Kind kind = Kind.valueOfName(multipartFile.getContentType()); // multipartFile.part.fileItem.contentTyoe
        Long size = multipartFile.getSize();
        File file = File.createFile(userRepository.findById(userId).get(), type, fileName, kind, size);

        return fileRepository.save(file).getId();
    }
}
