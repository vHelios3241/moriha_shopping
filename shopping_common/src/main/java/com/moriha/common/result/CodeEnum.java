package com.moriha.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeEnum {

    SUCCESS(200, "成功"),
    SYSTEM_ERROR(500, "系统异常"),
    PARAMETER_ERROR(400, "参数异常"),
    ;

    private final Integer code;
    private final String message;

}
