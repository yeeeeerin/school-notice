package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MessageDao {


    /*
    콘텐츠 문장을 꾸며줌
     */
    public String fullText(String selectBtn, String bbsConfigFK , Elements list){

        String fullStr =
                "서울여대 공지사항 알람봇 입니다. \n" +
                        "공지 사항은 고정 게시물과 상위 10가지만 보여지며 링크 클릭 시 해당하는 공지사항으로 이동됩니다. \n\n"+
                        "<" + selectBtn + "> \n";

        for(int i=0;i<list.size();i++){

            Elements element = list.eq(i);

            //각 공지사항의 타이틀을 가져옴
            String title = element.text();


            //해당하는 개시판의 id값 찾기
            String boardIdAttr = element.select("a").attr("onclick");

            int sIndex = boardIdAttr.indexOf(",") + 2;
            int eIndex = boardIdAttr.indexOf(";") - 2;

            String boardId = boardIdAttr.substring(sIndex,eIndex);


            fullStr += (i+1) +". "+ title + "\n"
                    + "http://www.swu.ac.kr/gopage/goboard1.jsp?bbsConfigFK="+bbsConfigFK+"&pkid=" + boardId + "\n\n";
        }



        return fullStr;
    }

    /*
    공지사항에 대한 크롤링을 담당하는 함수
     */
    public Elements pageCrawling(String pageUrl) throws Exception{

        Document url = Jsoup.connect(pageUrl).get();

        Elements tagVal = url.select("td.title");

        return tagVal;

    }

    /*
    식단표에 대한 크롤링을 담당하는 함
     */
    public String restaurant() throws Exception {
        Document url = Jsoup.connect("https://bds.bablabs.com/restaurants?campus_id=AwsaqReGHK").get();

        Elements tagVal = url.select("img.img-fluid");

        String imgSrc = tagVal.eq(1).attr("src");

        return imgSrc;
    }
}
