package ck.infrastructure.notify.impl;

import ck.infrastructure.notify.INotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements INotifyService {
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Override
    public void error(Exception exception) {
        threadPoolTaskExecutor.execute(()->{
            log.error("未知异常", exception);
        });
    }

    @Override
    public void error(String message, Exception e, Object... args) {
        log.error(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        log.warn(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        log.info(message, args);
    }
}
