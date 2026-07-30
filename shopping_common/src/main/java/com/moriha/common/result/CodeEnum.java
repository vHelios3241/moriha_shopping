package com.moriha.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeEnum {

    SUCCESS(200, "成功")
    ;

    private final Integer code;
    private final String message;

}
