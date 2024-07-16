package com.vanlang.webbanhang.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String image;

    private String manufacturer;
    private LocalDate releaseDate; // Sử dụng LocalDate thay vì String
    private String color;
    private String warranty;
}
