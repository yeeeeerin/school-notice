package com.example.demo.controller;

import com.example.demo.domain.Keyboard;
import com.example.demo.domain.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



@RestController
public class Controller {

    @Autowired
    Keyboard keyboard;

    @Autowired
    MessageDao messageDao;

    //produces="text/plain;charset=UTF-8" (한글 처리 관련)
    @RequestMapping(value = "/keyboard",method = RequestMethod.GET,produces="text/plain;charset=UTF-8")
    public String keyboard(){
        System.out.println("/keyboard");

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type",keyboard.getType());
        jsonObject.put("buttons",keyboard.getBtns());

        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST, headers = {"Content-type=application/json"},produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String message(@RequestBody String resObj) throws Exception {

        System.out.println("/message");

        String content;

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(resObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObj = (JSONObject) obj;

        content = (String) jsonObj.get("content"); //무슨 버튼을 눌렀는지 가져옴

        System.out.println(content);

        JSONObject jobjRes = new JSONObject();
        JSONObject jobjText = new JSONObject();
        JSONObject jobjmesBtn = new JSONObject();
        JSONObject jobjBtn = new JSONObject();

        jobjBtn.put("type", keyboard.getType());
        jobjBtn.put("buttons", keyboard.getBtns());


        Message message = messageDao.create(content);

        //버튼 별 작동
        if(content.contains("전체공지사항") || content.contains("전체")){
            System.out.println("click 전체공지사항");

            jobjmesBtn.put("label",message.getMessageBtn().getLabel());
            jobjmesBtn.put("url",message.getMessageBtn().getUrl());

            jobjText.put("message_button",jobjmesBtn);


        }  else if(content.contains("샬롬 식단")){
            System.out.println("click 샬롬 식단");

            JSONObject jobjMenuImg = new JSONObject();

            //식단 이미지 저장
            try {

                jobjMenuImg.put("url",message.getPhoto().getUrl());

                //사이즈
                jobjMenuImg.put("width",message.getPhoto().getWidth());
                jobjMenuImg.put("height",message.getPhoto().getHeight());

                //메세지 안에 있는 버튼
                jobjmesBtn.put("label",message.getMessageBtn().getLabel());
                jobjmesBtn.put("url",message.getMessageBtn().getUrl());

            }catch (Exception e){
                e.printStackTrace();
            }

            jobjText.put("message_button",jobjmesBtn);
            jobjText.put("photo",jobjMenuImg);

        }

        jobjText.put("text",message.getText());
        jobjRes.put("message", jobjText);
        jobjRes.put("keyboard", jobjBtn);

        return  jobjRes.toJSONString();
    }

}
