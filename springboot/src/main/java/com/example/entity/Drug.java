package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drug {
    private Integer id;
    private Integer hospitalId;
    private String drugNumber;
    private String drugName;
    private String price;
    private  String hospitalName;
}
