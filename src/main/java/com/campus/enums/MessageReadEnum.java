package com.campus.enums;

import lombok.Getter;

@Getter
public enum MessageReadEnum {
    UNREAD(0,"未读"),
    READ(1,"已读");
    private final Integer code;
    private final String text;
    MessageReadEnum(Integer code,String text){
        this.code = code;
        this.text = text;
    }

    public Integer getCode(){
        return code;
    }

    public String getText(){
        return text;
    }

    public static String getText(Integer code){
        for(MessageReadEnum item : values()){
            if(item.getCode().equals(code)){
                return item.getText();
            }
        }
        return "";
    }
}