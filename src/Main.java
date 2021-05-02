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
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Main {
    public static void main(String[] args) throws GeneralSecurityException, IOException {

        Frame frame = new Frame("PassManage", new Dimension(500, 500));
        frame.setVisible(true);


//        SecretKey secretKey = generateKey();
//
//        //ひとまず
//        if(false) {
//            //秘密鍵の作成・保存
//
//            FileOutputStream fos = new FileOutputStream("test.txt");
//            fos.write(secretKey.getEncoded());
//            fos.close();
//        }

        FileInputStream fis =  new FileInputStream("test.txt");//保存されている鍵（の種であるbyte配列）の取得



        SecretKey secretKey = generateKey(fis.readAllBytes());//この共通鍵は種をもとに作られます

        byte[] data = encrypto("データ", secretKey);
        System.out.println(decrypto(data, secretKey));


    }




}
