package com.product_management_service.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Names {
    PRODUCT_ATTRIBUTES("productAttributes"),
    PRODUCT_IMAGES("productImages"),
    NAME("name"),
    VALUE("value"),
    SUCCESS("success");
    public final String value;
}
