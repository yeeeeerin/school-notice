package com.example.demo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageDto {

    @JsonProperty("type")
    String type;
    @JsonProperty("content")
    String content;
}
