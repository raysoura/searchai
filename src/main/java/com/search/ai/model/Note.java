package com.search.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "notes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Note {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "custom_analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "custom_analyzer")
    private String content;

    @Field(type = FieldType.Dense_Vector, dims = 384) // adjust if using different embedding dimensions
    private float[] vectorEmbedding;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Date)
    private Instant updatedAt;

    // Methods to auto-populate createdAt and updatedAt
    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
