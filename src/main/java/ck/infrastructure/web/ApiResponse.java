package ck.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
     * 访问拒绝
     */
    public static ApiResponse<Void> forbidden() {
        return ApiResponse.<Void>builder().code(403).build();
    }
    /**
     * 需要授权
     */
    public static ApiResponse<Void> unauthorized() {
        return ApiResponse.<Void>builder().code(401).build();
    }
}
