package com.product_management_service.exceptions;

import lombok.*;

import java.io.Serializable;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Error implements Serializable {
    private ErrorCode errorCode;
    private String property;
    private String message;
}
