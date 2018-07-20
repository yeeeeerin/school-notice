package com.example.demo.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



@RestController
public class Controller {

    //produces="text/plain;charset=UTF-8" (한글 처리 관련)
    @RequestMapping(value = "/keyboard",method = RequestMethod.GET,produces="text/plain;charset=UTF-8")
    public String keyboard(){
        System.out.println("/keyboard");

        JSONObject jsonObject = new JSONObject();
        ArrayList<String> btns = new ArrayList<>();

        btns.add("전체공지사항");
        btns.add("학사");
        btns.add("장학");
        btns.add("행사");
        btns.add("채용/취업");
        btns.add("일반/봉사");
        btns.add("샬롬 식단");


        jsonObject.put("type","buttons");
        jsonObject.put("buttons",btns);

        return jsonObject.toJSONString();
    }

    //test code
    @RequestMapping(value = "/hi", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseBody
    public String testMessage(@RequestBody String resObj) throws ParseException {
        System.out.println("값들어간다." + resObj);


        JSONParser parser = new JSONParser();
        Object obj = parser.parse(resObj);
        JSONObject jsonObj = (JSONObject) obj;

        String code = (String) jsonObj.get("content");

        System.out.println(code);

        return "HI";
    }


    @RequestMapping(value = "/message", method = RequestMethod.POST, headers = {"Content-type=application/json"},produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String message(@RequestBody String resObj) {

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

        content = (String) jsonObj.get("content");

        System.out.println(content);

        JSONObject jobjRes = new JSONObject();
        JSONObject jobjText = new JSONObject();
        JSONObject jobjmesBtn = new JSONObject();
        JSONObject jobjBtn = new JSONObject();

        ArrayList<String> btns = new ArrayList<>();
        btns.add("전체공지사항");
        btns.add("학사");
        btns.add("장학");
        btns.add("행사");
        btns.add("채용/취업");
        btns.add("일반/봉사");
        btns.add("샬롬 식단");

        jobjBtn.put("type", "buttons");
        jobjBtn.put("buttons", btns);

        String text = "불러오지 못하였습니다.\n 다시 한번 시도해 주세요.\n 계속 작동하지 않는 경우 ebbunbul@swu.ac.kr로 메일 부탁드립니다.";

        //버튼 별 작동
        if(content.contains("전체공지사항") || content.contains("전체")){
            System.out.println("click 전체공지사항");
            jobjText.put("text","준비중 입니다! 조금만 기다려 주세요!");
            jobjmesBtn.put("label","학교 홈페이지 가기");
            jobjmesBtn.put("url"," http://www.swu.ac.kr");

            jobjText.put("message_button",jobjmesBtn);


        } else if(content.contains("학사")){
            System.out.println("click 학사");

            try {
                text = fullText("학사","4",pageCrawling("http://www.swu.ac.kr/front/boardlist.do?bbsConfigFK=4"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            jobjText.put("text",text);


        } else if(content.contains("장학")){

            System.out.println("click 장학");

            try {
                text = fullText("장학","5",pageCrawling("http://www.swu.ac.kr/front/boardlist.do?bbsConfigFK=5"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            jobjText.put("text",text);


        } else if(content.contains("행사")){

            System.out.println("click 행사");

            try {
                text = fullText("행사","6",pageCrawling("http://www.swu.ac.kr/front/boardlist.do?bbsConfigFK=6"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            jobjText.put("text",text);


        } else if(content.contains("취업") || content.contains("채용")){

            System.out.println("click 채용/취업");

            try {
                text = fullText("채용/취업","7",pageCrawling("http://www.swu.ac.kr/front/boardlist.do?bbsConfigFK=7"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            jobjText.put("text",text);


        } else if(content.contains("일반") || content.contains("봉사")){

            System.out.println("click 일반/봉사");

            try {
                text = fullText("일반/봉사","8",pageCrawling("http://www.swu.ac.kr/front/boardlist.do?bbsConfigFK=8"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            jobjText.put("text",text);


        } else if(content.contains("샬롬 식단")){
            System.out.println("click 샬롬 식단");

            JSONObject jobjMenuImg = new JSONObject();

            //식단 이미지 저장
            try {
                String s = restaurant();
                jobjMenuImg.put("url",s);
                System.out.println(s);
                //사이즈

                jobjMenuImg.put("width",480);
                jobjMenuImg.put("height",640);

            }catch (Exception e){
                e.printStackTrace();
            }

            text = "식단은 이미지로 표시됩니다.";


            jobjText.put("photo",jobjMenuImg);

            jobjText.put("text",text);

        }


        jobjRes.put("message", jobjText);
        jobjRes.put("keyboard", jobjBtn);
        System.out.println(jobjRes.toJSONString());

        return  jobjRes.toJSONString();
    }

    //content문장을 꾸며주는
    public String fullText(String selectBtn, String bbsConfigFK , Elements list){

        String fullStr =
                "서울여대 공지사항 알람봇 입니다. \n" +
                "공지 사항은 고정 게시물과 상위 10가지만 보여지며 링크 클릭 시 해당하는 공지사항으로 이동됩니다. \n\n"+
                "<" + selectBtn + "> \n";

        for(int i=0;i<list.size();i++){

            Elements element = list.eq(i);
            //System.out.println(element.html());
            //System.out.println("------------------------------------");

            //각 공지사항의 타이틀을 가져옴
            String title = element.text();
            //System.out.println("title" + title);


            //해당하는 개시판의 id값 찾기
            String boardIdAttr = element.select("a").attr("onclick");
            //System.out.println("a[onclick] = " + boardIdAttr);

            int sIndex = boardIdAttr.indexOf(",") + 2;
            int eIndex = boardIdAttr.indexOf(";") - 2;

            String boardId = boardIdAttr.substring(sIndex,eIndex);
            //System.out.println("boardId = " + boardId);


            fullStr += (i+1) +". "+ title + "\n"
                    + "http://www.swu.ac.kr/gopage/goboard1.jsp?bbsConfigFK="+bbsConfigFK+"&pkid=" + boardId + "\n\n";
        }

        //System.out.println("---------fullStr---------");
        //System.out.println(fullStr);


        return fullStr;
    }

    //식단표에 대한 크롤링을 담당하는 함
    public String restaurant() throws Exception {
        Document url = Jsoup.connect("https://bds.bablabs.com/restaurants?campus_id=AwsaqReGHK").get();
        //System.out.println(url.html());

        Elements tagVal = url.select("img.img-fluid");
        //System.out.println(tagVal.eq(1));

        String imgSrc = tagVal.eq(1).attr("src");
        //System.out.println(imgSrc);

        return imgSrc;
    }

    //공지사항에 대한 크롤링을 담당하는 함수
    public Elements pageCrawling(String pageUrl) throws Exception{

        Document url = Jsoup.connect(pageUrl).get();
        //System.out.println(url.body());


        Elements tagVal = url.select("td.title");
        //System.out.println("elements num = " + tagVal.size());


        /*
        ArrayList<String> titleList = new ArrayList<String>();

        //게시판 title을 가져와서 titleList에 저장
        for(int i=0;i<tagVal.size();i++){

            System.out.println(tagVal.eq(i).text());
            titleList.add(tagVal.eq(i).text());
        }


        String text= tagVal.select("span").html();
        System.out.println( "tagVal : " + tagVal );
        System.out.println( "text : " + text );


        */


        return tagVal;


    }



}
