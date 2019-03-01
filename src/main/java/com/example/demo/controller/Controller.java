package com.example.demo.controller;

import com.example.demo.domain.Keyboard;
import com.example.demo.domain.Message;
import com.example.demo.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.simple.parser.JSONParser;


@RestController
@Slf4j
public class Controller {

    @Autowired
    Keyboard keyboard;

    @Autowired
    MessageService messageService;

    //produces="text/plain;charset=UTF-8" (한글 처리 관련)
    @GetMapping(value = "/keyboard",produces="text/plain;charset=UTF-8")
    public String keyboard(){
        log.info("keyboard");
        log.info(keyboard.getType());

        return toStringResponseMaker(keyboard);
    }

    @PostMapping(value = "/message", headers = {"Content-type=application/json"},produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String message(@RequestBody String content) throws Exception {

        log.info("message");

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;

        content = (String) jsonObj.get("content"); //무슨 버튼을 눌렀는지만 가져옴

        log.info(content);

        Message message = messageService.create(content);

        return toStringResponseMaker(message);

    }

    /*
    serialization
     */
    String toStringResponseMaker(Object o){

        String result = "";
        try {
            result = new ObjectMapper()
                    .writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

}
