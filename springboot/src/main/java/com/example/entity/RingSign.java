package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RingSign {
    private String signData;
    private String signKey;
    private String message;
    /** 公钥环 */
    private String signPubKey;
}
