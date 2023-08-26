package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

}
