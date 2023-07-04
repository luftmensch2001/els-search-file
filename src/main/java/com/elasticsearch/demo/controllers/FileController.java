package com.elasticsearch.demo.controllers;

import com.elasticsearch.demo.DTO.FileDTO;
import com.elasticsearch.demo.models.FileEntity;
import com.elasticsearch.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public Iterable<FileEntity> listAll() {
        return fileService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Pair<Boolean, Object> result = fileService.findById(id);
        if (result.getFirst())
//            File found
            return ResponseEntity.ok(result.getSecond());
        else
//            Not found
            return ResponseEntity.badRequest().body(result.getSecond());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@ModelAttribute("file") MultipartFile file) {
        Pair<Boolean, Object> result = fileService.uploadFile(file);
        if (result.getFirst()) {
            return ResponseEntity.ok().body(result.getSecond());
        } else {
            return ResponseEntity.badRequest().body(result.getSecond());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByContent(@RequestParam String content) {
        List<FileDTO> result = fileService.findByContent(content);
        if (result.size() > 0) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body("Not found any file");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) {
        Pair<Boolean, Object> result = fileService.downloadFile(id);
        if (result.getFirst() == false) {
//            Not found
            return ResponseEntity.badRequest().body(result.getSecond());
        } else {
//            Return download response
            FileEntity file = (FileEntity) result.getSecond();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(new ByteArrayResource(file.getData()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Integer id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok("Deleted file");
    }
}
