package ck.infrastructure.validator;

import lombok.AllArgsConstructor;

import java.util.Objects;
/**
 * 相等校验器
 */
@AllArgsConstructor
public class NotEqualsValidator<E> implements Runnable{
    private final E object1;
    private final E object2;
    private final RuntimeException exception;
    @Override
    public void run() {
        if(!Objects.equals(object1, object2)){
            throw exception;
        }
    }
}
