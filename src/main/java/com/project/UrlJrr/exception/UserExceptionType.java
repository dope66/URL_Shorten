package com.project.UrlJrr.exception;

import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType{
    ALREADY_EXIST_USERNAME(600, HttpStatus.OK, "이미 존재하는 아이디입니다."),
    WRONG_PASSWORD(601,HttpStatus.OK, "비밀번호가 잘못되었습니다."),
    NOT_FOUND_MEMBER(602, HttpStatus.OK, "회원 정보가 없습니다."),
    ALREADY_EXIST_EMAIL(603, HttpStatus.OK, "이미 존재하는 이메일입니다."),

    WRONG_FORM_PASSWORD(604,HttpStatus.OK,"비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용해야 합니다.");



    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    UserExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

}
