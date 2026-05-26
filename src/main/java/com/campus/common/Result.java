package com.campus.common;

import lombok.Data;

//统一返回类
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    //带数据成功
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }
    //无数据成功
    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }
    //业务失败500
    public static <T> Result<T> error(String message){
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    //未登录401
    public static <T> Result<T> unauthorized(String message){
        Result<T> result = new Result<>();
        result.setCode(401);
        result.setMessage(message);
        return result;
    }
    //无权限403
    public static <T> Result<T> forbidden(String message){
        Result<T> result = new Result<>();
        result.setCode(403);
        result.setMessage(message);
        return result;
    }
}
