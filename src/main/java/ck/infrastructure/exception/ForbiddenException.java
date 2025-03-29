package ck.infrastructure.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 拒绝访问异常
 */
@Getter
@RequiredArgsConstructor
public class ForbiddenException extends RuntimeException {
    private final String message;
}
