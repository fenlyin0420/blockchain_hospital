package com.example.mapper;

public interface QRCodeMapper {
    int create(String uuid, String content);
    
    String read(String uuid);
}