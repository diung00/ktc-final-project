package com.example.ChoiGangDeliveryApp.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private GlobalErrorCode errorCode;


}
