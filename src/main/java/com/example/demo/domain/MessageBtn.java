package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageBtn {
    private String label;
    private String url;

    public MessageBtn(String label,String url){
        this.label = label;
        this.url = url;
    }
}
