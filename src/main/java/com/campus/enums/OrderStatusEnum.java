package com.campus.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    UNPAID(0,"待支付"),
    PAID(1,"已支付"),
    FINISHED(2,"已完成"),
    CANCELED(3,"已取消");
    private final Integer code;
    private final String text;
    OrderStatusEnum(Integer code,String text){
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
        for(OrderStatusEnum item : values()){
            if(item.getCode().equals(code)){
                return item.getText();
            }
        }
        return "";
    }
}
