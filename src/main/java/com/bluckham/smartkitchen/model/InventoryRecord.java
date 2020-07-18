package com.bluckham.smartkitchen.model;

import java.time.LocalDate;

import lombok.Data;

public @Data class InventoryRecord {
    private Double weight;
    private Integer count;
    private String name;
    private String description;
    private LocalDate expiration;
}