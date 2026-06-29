package com.example.common;


import com.example.common.enums.ResultCodeEnum;

public class Result<T> {
    private String code;
    private String msg;
    private T data;

    private Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setCode(ResultCodeEnum.SUCCESS.code);
        result.setMsg(ResultCodeEnum.SUCCESS.msg);
        return result;
    }

    public static <T> Result<T> error() {
        return error(ResultCodeEnum.SYSTEM_ERROR);
    }

    public static <T> Result<T> error(String code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> error(ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        result.setCode(resultCodeEnum.code);
        result.setMsg(resultCodeEnum.msg);
        return result;
    }

    public static <T> Result<T> failure(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}