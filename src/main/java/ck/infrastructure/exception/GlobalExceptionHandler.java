package ck.infrastructure.exception;

import ck.infrastructure.notify.INotifyService;
import ck.infrastructure.web.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常拦截器
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final INotifyService notifyService;

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<Void> exception(NoResourceFoundException e){
        return ApiResponse.notFound();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<Void> exception(ForbiddenException e){
        return ApiResponse.forbidden();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<Void> exception(UnauthorizedException e){
        return ApiResponse.unauthorized();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<Void> exception(BizException e){
        return ApiResponse.error(e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<Void> exception(Exception e){
        notifyService.error(e);
        return ApiResponse.error();
    }
}
