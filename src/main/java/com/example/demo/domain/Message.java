package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
   private MessageBtn messageBtn;
   private Photo photo;
   private String text;

}
