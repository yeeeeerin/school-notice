package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;


/*
메세지 안에 있는 버튼을 만들어주는 객체
-식단표보기에서 누르면 식단표가 크게보이는 기능에서 쓰임
 */

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
