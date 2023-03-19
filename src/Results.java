import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Results extends Thread
{
    JFrame jFrame;
    private boolean win;

    public Results(boolean win)
    {
        jFrame = new JFrame();
        this.win = win;
    }

    @Override
    public void run() {
        JLabel title = new JLabel(win?"WYGRANA":"PRZEGRANA", SwingConstants.CENTER);
        title.setSize(400,200);
        title.setFont(new Font("Arial", Font.PLAIN, 30));

        JButton btn = new JButton("Wyjście");
        btn.setBounds(127, 400, 150, 50);
        btn.addActionListener(e ->
        {
            jFrame.dispose();
        });

        jFrame.setSize(400, 500);
        jFrame.setLayout(null);
        jFrame.add(title);
        jFrame.add(btn);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
    }
}
