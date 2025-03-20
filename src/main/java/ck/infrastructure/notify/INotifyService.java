package ck.infrastructure.notify;

/**
 * 通知接口
 */
public interface INotifyService {
    /**
     * 异常通知
     */
    void error(Exception exception);
    /**
     * 异常信息
     */
    void error(String message, Exception e, Object... args);
    /**
     * 告警信息
     */
    void warn(String message, Object... args);

    /**
     * 正常信息
     */
    void info(String message, Object... args);
}
