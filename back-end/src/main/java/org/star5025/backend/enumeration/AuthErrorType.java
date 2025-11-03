package org.star5025.backend.enumeration;


public enum AuthErrorType {
    USERNAME_NOT_FOUND("用户名不存在"),
    PASSWORD_INCORRECT("密码输入错误");

    private final String message;

    AuthErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
