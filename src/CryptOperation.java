import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CryptOperation {//シングルトン

    private static final CryptOperation INSTANCE = new CryptOperation();

    private CryptOperation() {
    }

    public static CryptOperation getInstance() {
        return INSTANCE;
    }


    public SecretKey generateKey() {

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(128);

        return keyGen.generateKey();
    }

    public SecretKey generateKey(byte[] seed) {
        return new SecretKeySpec(seed, "AES");
    }


    //暗号
    public byte[] encrypto(String plainText, SecretKey key) throws GeneralSecurityException {
        // 書式:"アルゴリズム/ブロックモード/パディング方式"
        Cipher encrypter = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encrypter.init(Cipher.ENCRYPT_MODE, key);

        return encrypter.doFinal(plainText.getBytes());
    }

    //復号
    public String decrypto(byte[] cryptoText, SecretKey key) throws GeneralSecurityException, UnsupportedEncodingException {
        // 書式:"アルゴリズム/ブロックモード/パディング方式"
        Cipher decrypter = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decrypter.init(Cipher.DECRYPT_MODE, key);

        return new String(decrypter.doFinal(cryptoText), "UTF-8");
    }
}
