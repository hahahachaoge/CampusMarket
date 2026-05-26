package com.campus.enums;

import lombok.Getter;

@Getter
public enum GoodsStatusEnum {
    ON_SALE(1,"在售"),
    OFF_SALE(2,"已下架"),
    SOLD(3,"已售出");
    private final Integer code;
    private final String text;

    GoodsStatusEnum(Integer code,String text){
        this.code = code;
        this.text = text;
    }

    public static String getText(Integer code){
        for(GoodsStatusEnum item : values()){
            if(item.getCode().equals(code)){
                return item.getText();
            }
        }
        return "";
    }
}
