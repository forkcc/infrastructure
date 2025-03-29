package ck.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * Api响应包装
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<E> {
    private int code;
    private String message;
    private E data;

    /**
     * 资源不存在
     */
    public static ApiResponse<Void> notFound() {
        return ApiResponse.<Void>builder().code(404).build();
    }
    /**
     * 成功
     */
    public static ApiResponse<Void> ok() {
        return ApiResponse.<Void>builder().code(200).build();
    }
    /**
     * 成功
     */
    public static <E>ApiResponse<E> ok(E e) {
        return ApiResponse.<E>builder().code(200).data(e).build();
    }
    /**
     * 访问拒绝
     */
    public static ApiResponse<Void> forbidden(String message) {
        return ApiResponse.<Void>builder().code(403).message(message).build();
    }
    /**
     * 需要授权
     */
    public static ApiResponse<Void> unauthorized() {
        return ApiResponse.<Void>builder().code(401).build();
    }

    /**
     * 错误通知
     */
    public static ApiResponse<Void> error() {
        return ApiResponse.<Void>builder().code(500).build();
    }

    /**
     * 错误通知
     */
    public static ApiResponse<Void> error(String message) {
        return ApiResponse.<Void>builder().message(message).code(500).build();
    }
}
