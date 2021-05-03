import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Frame extends JFrame implements ActionListener {

    final String geneButtonName = "GenerateKey";
    final String cryButtonName = "cryptoButton";
    JButton generateButton;
    JButton cryptoButton;
    JLabel keyDrop;
    JLabel ciphertextDrop;


    final String keyName = "//key.key";

    public Frame(String title, Dimension windowSize) {
        super(title);
        this.setSize(new Dimension(windowSize));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));//土台のレイアウトはボックスレイアウト

        layoutSet();

    }

    private void layoutSet() {

        generateButton = new JButton(geneButtonName);
        cryptoButton = new JButton(cryButtonName);

        cryptoButton.setEnabled(false);
        //イベントリスナーをつける
        generateButton.addActionListener(this);
        cryptoButton.addActionListener(this);
        //位置を決める
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cryptoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //パネルに配置する
        this.add(generateButton);
        this.add(cryptoButton);


        keyDrop = new JLabel("キーをここにドロップしてください");
        ciphertextDrop = new JLabel("暗号文をここにドロップしてください");
        //フォントを指定
        keyDrop.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 20));
        ciphertextDrop.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 20));

        //ボーダーを設定
        LineBorder border = new LineBorder(Color.BLACK, 2, true);
        ciphertextDrop.setBorder(border);
        keyDrop.setBorder(border);

        //ドロップ機能をつける
        ciphertextDrop.setTransferHandler(new Handle());
        keyDrop.setTransferHandler(new Handle());
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
        subPanel.add(new JLabel(" "));
        subPanel.add(ciphertextDrop);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand() + "ボタンが押されました");

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
                    CryptOperation cryptOperation = CryptOperation.getInstance();
                    SecretKey secretKey = cryptOperation.generateKey();//共通鍵の生成
                    try (FileOutputStream fos = new FileOutputStream(path);) {//try with resouceを使用
                        fos.write(secretKey.getEncoded());
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "鍵ファイルがすでに存在します\n新たに鍵を生成する場合は既存の鍵を別ディレクトリに移す\nあるいはリネームしてください", "エラー", JOptionPane.YES_OPTION);
                }


                break;


            case cryButtonName:

                break;

        }
    }


    class Handle extends TransferHandler {
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


        @Override
        public boolean importData(TransferSupport support) {
            // 受け取っていいものか確認する
            if (!canImport(support)) {
                return false;
            }

            // ドロップ処理
            Transferable t = support.getTransferable();
            try {
                // ファイルを受け取る
                List<File> files = (List) t.getTransferData(DataFlavor.javaFileListFlavor);

                // テキストエリアに表示するファイル名リストを作成する


                System.out.println(files.get(0).getName());

                // FileInputStream fis = new FileInputStream(files.get(0));

                try (BufferedReader br = Files.newBufferedReader(files.get(0).toPath())) {
                    String text;
                    while ((text = br.readLine()) != null) {
                        System.out.println(text);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

    }

}