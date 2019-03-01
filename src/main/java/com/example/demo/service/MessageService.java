package com.example.demo.service;

import com.example.demo.domain.Message;
import com.example.demo.domain.MessageBtn;
import com.example.demo.domain.Photo;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    /*
    메세지를 만들어주는 함수
     */
    public Message create(String selectBtn) throws Exception {

        Message message = new Message();
        Elements elements;

        String text = "불러오지 못하였습니다.\n 다시 한번 시도해 주세요.\n 계속 작동하지 않는 경우 ebbunbul@swu.ac.kr로 메일 부탁드립니다.";
        String bbsConfigFK = "";

        if(selectBtn.contains("학사")){
            bbsConfigFK="4";
        }else if (selectBtn.contains("장학")){
            bbsConfigFK="5";
        }else if (selectBtn.contains("행사")){
            bbsConfigFK="6";
        }else if (selectBtn.contains("취업") || selectBtn.contains("채용")){
            bbsConfigFK="7";
        }else if (selectBtn.contains("일반") || selectBtn.contains("봉사")){
            bbsConfigFK="8";
        }


        if(selectBtn.contains("샬롬 식단")){

            String s = restaurant();

            //식단표가 업로드 되어있지 않다면?
            if(s.equals("")) {
                text = "식단표가 업로드 되어있지 않습니다.";
            }else {

                MessageBtn messageBtn = new MessageBtn("식단표 크게 보기", s);
                message.setMessageBtn(messageBtn);

                Photo photo = new Photo(s, 682, 1024);
                message.setPhoto(photo);

                text = "";
            }

        }else if (selectBtn.contains("전체")){

            MessageBtn messageBtn = new MessageBtn("학교 홈페이지 가기","http://www.swu.ac.kr");
            message.setMessageBtn(messageBtn);

            text = "준비중 입니다! 조금만 기다려 주세요!";

        }else if(!text.equals("")){

            elements = pageCrawling("http://www.swu.ac.kr/front/boardlist.do?bbsConfigFK="+
                    bbsConfigFK);
            text = fullText(selectBtn,bbsConfigFK,elements);
        }

        message.setText(text);

        return message;
    }

    /*
    콘텐츠 문장을 꾸며줌
     */
    private String fullText(String selectBtn, String bbsConfigFK , Elements list){

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
    private Elements pageCrawling(String pageUrl) throws Exception{

        Document url = Jsoup.connect(pageUrl).get();

        Elements tagVal = url.select("td.title");

        return tagVal;

    }

    /*
    식단표에 대한 크롤링을 담당하는 함
     */
    private String restaurant() throws Exception {

        Document url_origin = Jsoup.connect("http://dorm.swu.ac.kr/bbs/bbs/?bbs_no=7").get();
        Elements tag = url_origin.select("a.btnRead");
        String t = "http://dorm.swu.ac.kr/bbs/bbs/view.php?bbs_no=7&data_no="+tag.eq(0).attr("value")+"&page_no=1&sub_id=";

        Document url = Jsoup.connect(t).get();
        String imgSrc = "http://dorm.swu.ac.kr"+url.select("img[src^=/skin/upload]").attr("src");

        return imgSrc;
    }
}
