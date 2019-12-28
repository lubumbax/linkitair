package com.lubumbax.linkitair.flights.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document(collection = "parents")
public class Parent {
    @Id
    private String id;
    private String name;
    private Child child;

    @Data
    @Builder
    public static class Child {
        @MongoId
        private String id;
        private String name;
        private String key;
    }
}
