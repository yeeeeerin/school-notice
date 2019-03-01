package com.example.demo.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Photo {
    private String url;
    private int width;
    private int height;
}
