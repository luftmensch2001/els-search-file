package com.elasticsearch.demo.controllers;

import com.elasticsearch.demo.DTO.FileDTO;
import com.elasticsearch.demo.models.FileEntity;
import com.elasticsearch.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
    public List<FileDTO> searchByContent(@RequestParam String content) {
        return fileService.findByContent(content);
    }
}
