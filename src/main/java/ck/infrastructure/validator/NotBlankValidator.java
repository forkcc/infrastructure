package ck.infrastructure.validator;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * 非空校验器
 */
@AllArgsConstructor
public class NotBlankValidator implements Runnable{
    private final String str;
    private final RuntimeException exception;
    @Override
    public void run() {
        if(Objects.isNull(str) || str.trim().isEmpty()){
            throw exception;
        }
    }
}
