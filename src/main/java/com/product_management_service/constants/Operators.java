package com.product_management_service.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Operators {
    POSITIONAL_UPDATE_OPERATOR("$");
    public final String value;
}
