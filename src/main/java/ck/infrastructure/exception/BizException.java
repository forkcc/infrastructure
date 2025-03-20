package ck.infrastructure.exception;

public class BizException extends RuntimeException{
    public BizException(String message) {
        super(message);
    }
}
