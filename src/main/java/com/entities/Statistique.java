package com.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Setter
@Getter
@Document("statistiques")
@AllArgsConstructor
@NoArgsConstructor
public class Statistique {

    @Id
    private String id;

    @Field("pizzas")
    private Map<String, Long> pizzas;

    @Field("ingredients")
    private Map<String, Long> ingredients;

    @Field("utilisateurs")
    private Map<String, Long> utilisateurs;
}
