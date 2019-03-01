package com.example.demo.controller;

import com.example.demo.controller.dto.MessageDto;
import com.example.demo.domain.Keyboard;
import com.example.demo.domain.Message;
import com.example.demo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class Controller {

    @Autowired
    Keyboard keyboard;

    @Autowired
    MessageService messageService;

    //produces="text/plain;charset=UTF-8" (한글 처리 관련)
    @GetMapping
    public Keyboard keyboard(){
        log.info("keyboard");
        log.info(keyboard.getType());

        return keyboard;
    }

    @PostMapping
    public Message message(@RequestBody MessageDto dto) throws Exception {

        log.info("message");
        log.info(dto.getContent());

        Message message = messageService.create(dto.getContent());

        return message;

    }

}
