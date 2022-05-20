package io.springbootlabs.app.web.in;

import io.springbootlabs.domain.exception.StorageFileNotFoundException;
import io.springbootlabs.domain.usecase.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileStorage fileStorage;

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files",
                fileStorage.loadAll().map(
                        path ->
                                MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                        "serveFile",
                                        path.getFileName().toString()).build().toUri().toString()
                ).collect(Collectors.toList())
        );
        return "file/uploadform";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileStorage.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION, // 리소스가 존재하면, 응답해더에 이걸 붙여서 브라우저가 다운로드하게 한다.
                        "attachment; filename=\"" + file.getFilename() + "\""
                )
                .body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        fileStorage.store(file);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded" + file.getOriginalFilename() + "!");
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException storageFileNotFoundException) {
        return ResponseEntity.notFound().build();
    }
}
