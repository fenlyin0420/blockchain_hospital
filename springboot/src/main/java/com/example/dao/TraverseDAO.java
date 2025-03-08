package com.example.dao;

import com.example.entity.Traverse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TraverseDAO extends Traverse{
    private String signPubKey;
    private String signData;
}
