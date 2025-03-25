package ck.infrastructure.safety;

import ck.infrastructure.exception.ForbiddenException;
import ck.infrastructure.validator.NotBlankValidator;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Objects;

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalRequestBodyAdvice extends RequestBodyAdviceAdapter{
    private final RSAKIt rsakIt;
    private final HttpServletRequest request;
    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,@NonNull Type targetType,@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return Objects.isNull(methodParameter.getMethodAnnotation(NoEncrypted.class));
    }

    @Override
    @SneakyThrows
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String aesKey = inputMessage.getHeaders().getFirst("aesKey");
        new NotBlankValidator(aesKey, new ForbiddenException()).run();
        aesKey = rsakIt.decrypt(aesKey);
        request.setAttribute("aesKey", aesKey);
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(inputMessage.getBody().readAllBytes());
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new HttpInputMessage(){
            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(decryptedBytes);
            }
        };
    }

}
