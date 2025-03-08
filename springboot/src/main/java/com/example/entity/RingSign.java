package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RingSign {
    /** 签名数据 */
    private String signData;
    /** 签名消息 */
    private String message;
    /** 公钥环 */
    private String signPubKey;
}
