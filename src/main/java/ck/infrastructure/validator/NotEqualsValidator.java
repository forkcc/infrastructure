package ck.infrastructure.validator;

import lombok.AllArgsConstructor;

import java.util.Objects;
/**
 * 相等校验器
 */
@AllArgsConstructor
public class NotEqualsValidator implements Runnable{
    private final Object object1;
    private final Object object2;
    private final RuntimeException exception;
    @Override
    public void run() {
        if(!Objects.equals(object1, object2)){
            throw exception;
        }
    }
}
