package com.elasticsearch.demo.services;

import com.elasticsearch.demo.DTO.FileDTO;
import com.elasticsearch.demo.models.FileEntity;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    public Iterable<FileEntity> listAll();

    public Pair<Boolean, Object> uploadFile(MultipartFile file);

    public List<FileDTO> findByContent(String content);

    public Pair<Boolean, Object> downloadFile(Integer id);

    public Pair<Boolean, Object> findById(Integer id);

    public void deleteFile(Integer id);
}
