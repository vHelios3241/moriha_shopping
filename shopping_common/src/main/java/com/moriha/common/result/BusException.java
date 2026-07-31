package com.moriha.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
//没写extends RuntimeException 导致@ExceptionHandler(BusException.class)处报错不兼容
public class BusException extends RuntimeException {
    private Integer code;
    private String msg;

    public BusException(CodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMessage();
    }
}
