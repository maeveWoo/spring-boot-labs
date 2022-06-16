package io.springbootlabs.domain.file.service;

import io.springbootlabs.domain.file.File;
import io.springbootlabs.domain.file.Kind;
import io.springbootlabs.domain.file.Type;
import io.springbootlabs.domain.usecase.FileHandling;
import io.springbootlabs.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TestInmemoryFileService implements FileHandling {
    private final UserRepository userRepository;
    @Override
    public List<File> findListByUser(Long userId) {
        List<File> testList = new ArrayList();
        testList.add(File.createFile(userRepository.findById(userId).get(), Type.Image, "test1", Kind.PNG, 2000L));
        testList.add(File.createFile(userRepository.findById(userId).get(), Type.Image, "test2", Kind.PNG, 7700L));
        testList.add(File.createFile(userRepository.findById(userId).get(), Type.Image, "test3", Kind.PNG, 4000L));
        testList.add(File.createFile(userRepository.findById(userId).get(), Type.Image, "test4", Kind.PNG, 5100L));
        testList.add(File.createFile(userRepository.findById(userId).get(), Type.Image, "test5", Kind.PNG, 5500L));

        return testList;
    }

    @Override
    public File findBy(Long fileId) {
        return null;
    }

    @Override
    public Long save(String fileName, MultipartFile multipartFile) {
        return null;
    }
}
