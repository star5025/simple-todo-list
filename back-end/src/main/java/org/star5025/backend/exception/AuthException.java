package org.star5025.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.star5025.backend.enumeration.AuthErrorType;

@Data
@NoArgsConstructor
public class AuthException extends RuntimeException {
    private AuthErrorType authErrorType;

    private String detail;

    public AuthException(AuthErrorType authErrorType) {
        super(authErrorType.getMessage());
        this.authErrorType = authErrorType;
    }

    public AuthException(AuthErrorType authErrorType, String detail) {
        super(authErrorType.getMessage() + "*原因: " + detail);
        this.authErrorType = authErrorType;
        this.detail = detail;
    }
}
