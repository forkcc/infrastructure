package ck.infrastructure.validator;

import lombok.AllArgsConstructor;

import java.util.Objects;
/**
 * 非空校验器
 */
@AllArgsConstructor
public class NotNullValidator<E> implements Runnable{
    private final E object;
    private final RuntimeException exception;
    @Override
    public void run() {
        if(Objects.isNull(object)){
            throw exception;
        }
    }
}
