package com.product_management_service.constants;

public class MessageConstants {

    public static final String MANDATORY_HEADER_MISSING = "Missing Mandatory Header ";

    public static final String HEADER_MISSING = "Missing request header";

    public static final String INVALID_MISSING = " value is empty or invalid or missing";
    public static final String INVALID = " value is invalid ";

    public static final String INTERNAL_SERVER_ERROR = "Operation failed because of an internal network error";

    public static final String MALFORMED_REQUEST = "Malformed Promise Payload";

    public static final String METHOD_NOT_ALLOWED = "Method Not Allowed";
    public static final String VALUE_OVER_FLOW = "Length exceeds";
    public static final String MEDIA_TYPE_NOT_SUPPORTED = "Only application/json is supported in request body";
    MessageConstants() {
        throw new IllegalStateException("Utility class");
    }
}
