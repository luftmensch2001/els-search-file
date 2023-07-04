package com.elasticsearch.demo.repositories;

import com.elasticsearch.demo.models.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface FileRepository extends JpaRepository<FileEntity, Integer> {
}
