import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame implements ActionListener {

    public Frame(String title, Dimension windowSize){
        super(title);
        this.setSize(new Dimension(windowSize));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton generateButton = new JButton("GenerateButton");
        generateButton.addActionListener(this);
        this.add(generateButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
