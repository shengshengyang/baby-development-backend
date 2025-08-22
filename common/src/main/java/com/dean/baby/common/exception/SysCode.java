package com.dean.baby.common.exception;


public enum SysCode implements ErrorCode{

    OK(200, "Success"),
    USER_NOT_FOUND(1001, "User not found"),
    UNEXPECTED_RESPONSE_TYPE(1002, "unexpected response type"),
    CLOCK_IN_FAILED(1003, "unexpected response type"),
    AUTHENTICATION_FAILED(1004, "Authentication failed"),
    SHIFT_NOT_FOUND(1005, "Shift not found"),
    UPDATE_FAILED(1006, "Failed to update user"),
    EMAIL_MISMATCH(1007, "Email does not match"),
    EMAIL_SEND_FAILED(1008, "Failed to send email"),
    INVALID_ARGUMENT_VALUE(1009, "Invalid argument value"),
    MISSING_PARAMETER(1010, "Missing parameter"),
    BAD_REQUEST(400, "Bad Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    FILE_PROCESSING_ERROR(10011, "File processing error"),
    COMPANY_NAME_ALREADY_EXISTS(10012, "Company name already exists"),
    COMPANY_NOT_FOUND(10013, "Company not found"),
    JWT_VALIDATION_FAILED(10014, "JWT validation failed"),
    CACHE_CODE_NOT_MATCH_OR_EXPIRED(10015, "Cache code not match or expired"),
    USER_ALREADY_EXISTS(10016, "User already exists"),
    NOT_LOGIN(10017, "Not login"),
    NOT_YOUR_BABY(10018, "Not your baby"),
    BABY_NOT_FOUND(10019, "Baby not found"),
    REGISTER_FAILED(10020, "Register failed, please try again later"),
    ROLE_NOT_FOUND(10021, "Role not found"),
    MILESTONE_NOT_FOUND(10022, "Milestone not found"),
    CATEGORY_NOT_FOUND(10023, "Category not found"),
    BAD_CREDENTIALS(10024, "Bad credentials"),
    SYSTEM_ERROR(9999, "system error" );

    private final Integer code;
    private final String message;

    SysCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public Integer getCode() {
        return 0;
    }
}
