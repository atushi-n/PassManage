import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

public class Frame extends JFrame implements ActionListener {

    final String geneButtonName = "GenerateKey";
    final String encryptButtonName = "encrypt";
    final String decryptButtonName = "decrypt";

    JButton generateButton;
    JButton encryptButton;
    JButton decryptButton;
    JLabel keyDrop;
    JLabel ciphertextDrop;

    JTextArea textArea;

    CryptOperation cryptOperation;

    List<File> keys;
    List<File> plaintexts;
    final String keyName = "//key.key";

    public Frame(String title, Dimension windowSize) {
        super(title);
        this.setSize(new Dimension(windowSize));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));//土台のレイアウトはボックスレイアウト

        layoutSet();
        cryptOperation = CryptOperation.getInstance();
    }

    private void layoutSet() {

        generateButton = new JButton(geneButtonName);
        encryptButton = new JButton(encryptButtonName);
        decryptButton = new JButton(decryptButtonName);

        //イベントリスナーをつける
        generateButton.addActionListener(this);
        encryptButton.addActionListener(this);
        decryptButton.addActionListener(this);
        //位置を決める
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        encryptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        decryptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //パネルに配置する
        this.add(generateButton);
        this.add(encryptButton);
        this.add(decryptButton);


        keyDrop = new JLabel("キーをここにドロップしてください");
        ciphertextDrop = new JLabel("暗号文をここにドロップしてください");
        textArea = new JTextArea();

        //フォントを指定
        keyDrop.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 20));
        ciphertextDrop.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 20));

        //ボーダーを設定
        LineBorder border = new LineBorder(Color.BLACK, 2, true);
        ciphertextDrop.setBorder(border);
        keyDrop.setBorder(border);

        //ドロップ機能をつける
        ciphertextDrop.setTransferHandler(new DropHandler() {
            @Override
            public boolean importData(TransferSupport support) {

                if (!canImport(support)) {// 受け取っていいものか確認する
                    return false;
                }

                // ドロップ処理
                Transferable t = support.getTransferable();
                try {
                    // ファイルを受け取る
                    plaintexts = (List) t.getTransferData(DataFlavor.javaFileListFlavor);

                    return true;
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        keyDrop.setTransferHandler(new DropHandler() {
            @Override
            public boolean importData(TransferSupport support) {

                if (!canImport(support)) {// 受け取っていいものか確認する
                    return false;
                }

                Transferable t = support.getTransferable();
                try {
                    // ファイルを受け取る
                    keys = (List) t.getTransferData(DataFlavor.javaFileListFlavor);

                    return true;
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });


        //位置を指定
        ciphertextDrop.setAlignmentX(Component.CENTER_ALIGNMENT);
        keyDrop.setAlignmentX(Component.CENTER_ALIGNMENT);

        //サブパネル
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));

        subPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPanel.setBackground(new
                Color(220, 220, 250));
        subPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));//十分に大きな値をセットする
        this.add(subPanel);

        //サブパネルにラベルを配置
        subPanel.add(keyDrop);
        subPanel.add(new JLabel(" "));//マージンをつけるのが面倒なので疑似マージン
        subPanel.add(ciphertextDrop);
        subPanel.add(new JLabel(" "));
        subPanel.add(textArea);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand() + "ボタンが押されました");

        SecretKey secretKey;
        byte[] data;

        switch (e.getActionCommand()) {

            case geneButtonName:

                String path = null;
                try {
                    path = Main.getApplicationPath(Main.class) + keyName;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                if (!new File(path).exists()) {//存在しなければ
                    System.out.println("共通鍵を生成します");
                    try (FileOutputStream fos = new FileOutputStream(path);) {//try with resouceを使用
                        fos.write(cryptOperation.generateKey().getEncoded());
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "鍵ファイルがすでに存在します\n新たに鍵を生成する場合は既存の鍵を別ディレクトリに移す\nあるいはリネームしてください", "エラー", JOptionPane.YES_OPTION);
                }
                break;



            case encryptButtonName:
                secretKey = cryptOperation.generateKey(getKeySeed());//ドロップされた鍵の種をもとに鍵を生成
                data = getData();


                try (FileOutputStream fos = new FileOutputStream("data.enc")) {
                    byte[] encData = cryptOperation.encrypto(new String(data), secretKey);
                    fos.write(encData);
                } catch (GeneralSecurityException | FileNotFoundException generalSecurityException) {
                    generalSecurityException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;


            case decryptButtonName:
                secretKey = cryptOperation.generateKey(getKeySeed());//ドロップされた鍵の種をもとに鍵を生成
                data = getData();


                try {
                    String plain = cryptOperation.decrypto(data, secretKey);
                    textArea.setText(plain);
                    System.out.println(plain);
                } catch (GeneralSecurityException generalSecurityException) {
                    generalSecurityException.printStackTrace();
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                }
                break;

        }
    }


    //暗号文あるいは平文の取得
    private byte[] getData() {
        try {
            FileInputStream fis = new FileInputStream(plaintexts.get(plaintexts.size() - 1));
            return fis.readAllBytes();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

    private byte[] getKeySeed() {
        try {
            FileInputStream fis = new FileInputStream(keys.get(keys.size() - 1));
            return fis.readAllBytes();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }


    class DropHandler extends TransferHandler {
        @Override
        public boolean canImport(TransferSupport support) {
            if (!support.isDrop()) {
                // ドロップ操作でない場合は受け取らない
                return false;
            }

            if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                // ドロップされたのがファイルでない場合は受け取らない
                return false;
            }
            return true;
        }


    }

}