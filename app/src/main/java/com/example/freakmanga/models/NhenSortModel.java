package com.example.freakmanga.models;

import lombok.Data;

@Data
public class NhenSortModel {
    private String sortName = "", sortValue = "";

    public NhenSortModel(String sortName, String sortValue) {
        this.sortName = sortName;
        this.sortValue = sortValue;
    }

}
