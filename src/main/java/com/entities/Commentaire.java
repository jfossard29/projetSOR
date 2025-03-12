package com.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "commentaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private String photo;
    private LocalDate date;
    private int note;

    @ManyToOne
    @JoinColumn(name = "id_pizza")
    @JsonBackReference("pizza-comments")
    private Pizza pizza;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @NotNull
    @JsonBackReference
    private User user;
}

