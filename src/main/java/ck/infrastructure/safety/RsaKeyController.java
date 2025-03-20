package ck.infrastructure.safety;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.charset.StandardCharsets;

/**
 * RSA密钥控制器
 */
@Tag(name = "RSA相关API")
@RestController
@RequiredArgsConstructor
public class RsaKeyController {
    private final RSAKIt rsakIt;
    @Operation(summary = "下载RSA公钥")
    @GetMapping(value = "rsa/public.key", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public StreamingResponseBody rsaPublicKey(){
        return out -> out.write(rsakIt.getPublicKey().getBytes(StandardCharsets.UTF_8));
    }
}
