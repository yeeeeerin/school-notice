package com.example.demo.controller;

import com.example.demo.domain.Keyboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class DaoFactory {
    @Bean
    public Keyboard keyboard(){
        Keyboard keyboard = new Keyboard();

        ArrayList<String> btns = new ArrayList<>();

        btns.add("전체공지사항");
        btns.add("학사");
        btns.add("장학");
        btns.add("행사");
        btns.add("채용/취업");
        btns.add("일반/봉사");
        btns.add("샬롬 식단");

        keyboard.setBtns(btns);
        keyboard.setType("buttons");

        return keyboard;
    }
}
