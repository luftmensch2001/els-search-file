package com.elasticsearch.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "file")
public class FileEntity {
    @Id
    public String id;

    public String name;

    public String type;

    public String content;


    public FileEntity(String name, String type, String content) {
        this.name = name;
        this.type = type;
        this.content = content;
    }
}
