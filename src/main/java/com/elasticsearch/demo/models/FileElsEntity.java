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
public class FileElsEntity {
    @Id
    public Integer id;

    public String name;

    public String type;

    public String content;
}
