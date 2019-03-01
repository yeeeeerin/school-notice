package com.example.demo.service;

import com.example.demo.domain.Message;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MessageServiceTest {

    @Test
    public void messageTestNotice() throws Exception {

        MessageService messageService = new MessageService();
        Message message = messageService.create("학사");
        assertThat(message.getText().contains("서울여대 공지사항 알람봇 입니다."),is(true));

    }

    @Test
    public void messageTestImg() throws Exception {
        MessageService messageService = new MessageService();
        Message message = messageService.create("샬롬 식단");
        assertThat(message.getPhoto().getUrl().contains("skin/upload"),is(true));
    }
}