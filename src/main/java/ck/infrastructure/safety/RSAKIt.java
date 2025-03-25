package ck.infrastructure.safety;

import ck.infrastructure.exception.BizException;
import ck.infrastructure.notify.INotifyService;
import ck.infrastructure.validator.NotBlankValidator;
import jakarta.servlet.http.HttpSession;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RSAKIt {
    private final static String ALGORITHM = "RSA";
    private final INotifyService notifyService;
    private final HttpSession session;
    private final static String PRIVATE_KEY = "RSAPrivateKey";
    private final static String PUBLIC_KEY = "RSAPublicKey";
    public RSAKIt(INotifyService notifyService, HttpSession session) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.notifyService = notifyService;
        this.session = session;
    }
    @SneakyThrows
    public KeyPair generateKeyPair(){
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(512, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public void savePrivateKey(KeyPair keyPair){
        session.setAttribute(PRIVATE_KEY, Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
    }
    public void savePublicKey(KeyPair keyPair){
        session.setAttribute(PUBLIC_KEY, Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }
    @SneakyThrows
    public String decrypt(String encryptedText){
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        return new String(cipher.doFinal(encryptedBytes));
    }
    @SneakyThrows
    private PrivateKey getPrivateKey(){
        String privateKey = (String) session.getAttribute(PRIVATE_KEY);
        new NotBlankValidator(privateKey, new BizException("请先获取密钥")).run();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }
    @SneakyThrows
    private PublicKey getPublicKey(){
        String privateKey = (String) session.getAttribute(PUBLIC_KEY);
        new NotBlankValidator(privateKey, new BizException("请先获取密钥")).run();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }
    @SneakyThrows
    public String encrypt(String text){
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
    }
}
