package com.product_management_service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PRODUCT_NOT_FOUND(INTERNAL_SERVER_ERROR, "System error"),
    CANNOT_BE_NULL(INTERNAL_SERVER_ERROR, "System error"),
    PRODUCT_VALIDATION_ERROR(BAD_REQUEST, "Validation error"),
    ENTITY_NOT_FOUND(NOT_FOUND, "Entity not found"),
    IMAGE_CONFLICT(CONFLICT, "Image already exists"),
    ATTRIBUTE_NOT_FOUND(INTERNAL_SERVER_ERROR,"System error"),
    ATTRIBUTE_CONFLICT(CONFLICT,"attribute already exists"),
    ATTRIBUTE_UPDATE_CONFLICT(CONFLICT,"updated attribute by same values"),
    PRODUCTS_INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "Operation failed because of an internal network error"),
    PRODUCTS_DOWNSTREAM_SERVICE_TIMEOUT_ERROR(SERVICE_UNAVAILABLE, "Downstream product Service timed out attempting operation"),
    PRODUCTS_MEDIA_TYPE_NOT_SUPPORTED(UNSUPPORTED_MEDIA_TYPE, "Media type not supported");
    private final HttpStatus httpStatus;
    private final String message;
}
