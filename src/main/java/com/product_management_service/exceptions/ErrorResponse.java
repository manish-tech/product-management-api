package com.product_management_service.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class ErrorResponse {
    private List<Error> errors = new ArrayList<>();
}
