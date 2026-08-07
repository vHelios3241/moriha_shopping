package com.moriha.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BaseResult<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    // 成功
    public static <T> BaseResult<T> ok(){
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), null);
    }

    //成功返回数据
    public static <T> BaseResult<T> ok(T data){
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), data);
    }
}
