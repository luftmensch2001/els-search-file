package com.elasticsearch.demo.repositories;

import com.elasticsearch.demo.models.FileElsEntity;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileElsRepository extends ElasticsearchRepository<FileElsEntity, Integer> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"content\": \"?0\"}}]}}")
    List<FileElsEntity> findByContent(String content);
}
