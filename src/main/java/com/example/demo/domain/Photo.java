package com.example.demo.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Photo {
    private String url;
    private int width;
    private int height;

    public Photo(String url, int width,int height){
        this.url = url;
        this.width = width;
        this.height = height;
    }
}
