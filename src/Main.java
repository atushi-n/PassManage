import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

public class Main {

    public static void main(String[] args) throws GeneralSecurityException, IOException, URISyntaxException {
        System.out.println(getApplicationPath(Main.class));




        Frame frame = new Frame("PassManage", new Dimension(500, 500));
        frame.setVisible(true);


//
//        FileInputStream fis =  new FileInputStream("test.txt");//保存されている鍵（の種であるbyte配列）の取得
//
//
//
//        SecretKey secretKey = generateKey(fis.readAllBytes());//この共通鍵は種をもとに作られます
//
//        byte[] data = encrypto("データ", secretKey);
//        System.out.println(decrypto(data, secretKey));


    }

    public static String getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        Path path = Paths.get(uri);
        return path.getParent().toString();
    }


}
