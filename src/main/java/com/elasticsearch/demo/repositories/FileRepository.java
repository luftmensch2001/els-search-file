package com.elasticsearch.demo.repositories;

import com.elasticsearch.demo.models.FileEntity;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileRepository extends ElasticsearchRepository<FileEntity, Long> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"content\": \"?0\"}}]}}")
    List<FileEntity> findByContent(String content);
}
