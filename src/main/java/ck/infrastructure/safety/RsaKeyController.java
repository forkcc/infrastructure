package ck.infrastructure.safety;

import ck.infrastructure.exception.ForbiddenException;
import ck.infrastructure.validator.EqualsValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * RSA密钥控制器
 */
@Tag(name = "RSA相关API")
@RestController
@RequiredArgsConstructor
public class RsaKeyController {
    private final RSAKIt rsakIt;
    @Operation(summary = "下载RSA公钥")
    @PostMapping(value = "rsa/public.key", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String rsaPublicKey(){
        return rsakIt.getPublicKey();
    }
    @Operation(summary = "测试加密后AES的Key")
    @PostMapping(value = "aes/key/test")
    public void aesKeyTest(@RequestBody AesKeyTest  aesKeyTest){
        new EqualsValidator<>(aesKeyTest.aesKeyOrigin, rsakIt.decrypt(aesKeyTest.aesKeyEncrypted), new ForbiddenException()).run();
    }

    @Data
    public static class AesKeyTest{
        @Schema(description = "测试的原始密码", requiredMode = Schema.RequiredMode.REQUIRED)
        private String aesKeyOrigin;
        @Schema(description = "测试的加密密码, Base64编码", requiredMode = Schema.RequiredMode.REQUIRED)
        private String aesKeyEncrypted;
    }
}
