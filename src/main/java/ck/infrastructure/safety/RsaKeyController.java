package ck.infrastructure.safety;

import ck.infrastructure.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * RSA密钥控制器
 */
@Tag(name = "RSA相关API")
@RestController
@RequiredArgsConstructor
public class RsaKeyController {
    private final RSAKIt rsakIt;
    @NoEncrypted
    @Operation(summary = "下载RSA公钥")
    @PostMapping(value = "/rsa/public.key", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String rsaPublicKey(){
        return rsakIt.getPublicKey();
    }



    @NoEncrypted
    @Operation(summary = "测试加密请求体, 只能用于测试")
    @PostMapping(value = "/aes/key/encrypt", produces = {MediaType.APPLICATION_JSON_VALUE},consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ApiResponse<List<String>> aesEncrypt(
            @Schema(example = "需要加密的内容填这里")
            @RequestBody String body,
            @RequestParam @Parameter(description = "AES的Key, 必须16位") String aesKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        List<String> strings = new ArrayList<>();
        strings.add(rsakIt.encrypt(aesKey));
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(), "AES");
        // 创建 Cipher 对象进行 AES 解密
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(body.getBytes(StandardCharsets.UTF_8));
        strings.add(Base64.getEncoder().encodeToString(decryptedBytes));
        return ApiResponse.ok(strings);

    }

}
