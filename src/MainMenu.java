import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class MainMenu extends Thread
{

    private final ArrayList<Level> levels = new ArrayList<>();
    private int chosenWidth;
    private int chosenHeigth;
    private int chosenMines;
    JFrame frame;

    public MainMenu()
    {
        frame = new JFrame();
        levels.add(new Level(10, 10, 15)); //easy
        levels.add(new Level(16, 16, 15)); //medium
        levels.add(new Level(30, 16, 20)); //hard
        chosenHeigth = levels.get(0).height;
        chosenWidth = levels.get(0).width;
        chosenMines = levels.get(0).mines;
    }
    @Override
    public void run() {
        JLabel title = new JLabel("SAPER", SwingConstants.CENTER);
        title.setSize(400,100);
        title.setFont(new Font("Arial", Font.PLAIN, 30));

        JLabel msg1 = new JLabel("Poziom trudności", SwingConstants.CENTER);
        msg1.setBounds(140,120,120,50);
        msg1.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel msg2 = new JLabel("Wymiary", SwingConstants.CENTER);
        msg2.setBounds(140,200,120,50);
        msg2.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel msg3 = new JLabel("x", SwingConstants.CENTER);
        msg3.setBounds(140,230,120,50);
        msg3.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel msg4 = new JLabel("Liczba min", SwingConstants.CENTER);
        msg4.setBounds(140,270,120,50);
        msg4.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel msg5 = new JLabel("%", SwingConstants.CENTER);
        msg5.setBounds(160,300,120,50);
        msg5.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel msg6 = new JLabel("", SwingConstants.CENTER);
        msg6.setBounds(0,350,400,50);
        msg6.setFont(new Font("Arial", Font.PLAIN, 12));
        msg6.setForeground(Color.red);

        JTextField widthField = new JTextField();
        widthField.setBounds(150, 246, 40, 20);
        widthField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        widthField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setValue();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                setValue();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                setValue();
            }
            public void setValue()
            {
                if (!widthField.getText().equals(""))
                {
                    chosenWidth = Integer.parseInt(widthField.getText());
                }
            }
        });

        JTextField heightField = new JTextField();
        heightField.setBounds(210, 246, 40, 20);
        heightField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        heightField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setValue();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                setValue();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                setValue();
            }
            public void setValue()
            {
                if (!heightField.getText().equals(""))
                {
                    chosenHeigth = Integer.parseInt(heightField.getText());
                }
            }
        });

        JTextField mineField = new JTextField();
        mineField.setBounds(170, 316, 40,20);
        mineField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        mineField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setValue();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                setValue();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                setValue();
            }
            public void setValue()
            {
                if (!mineField.getText().equals(""))
                {
                    chosenMines = Integer.parseInt(mineField.getText());
                }
            }
        });

        widthField.setText(String.valueOf(chosenWidth));
        heightField.setText(String.valueOf(chosenHeigth));
        mineField.setText(String.valueOf(chosenMines));
        widthField.setEditable(false);
        heightField.setEditable(false);
        mineField.setEditable(false);

        JButton btn = new JButton("Graj");
        btn.setBounds(152, 400, 100, 50);
        btn.addActionListener(e ->
        {
            if (chosenHeigth < 8 || chosenHeigth > 20 || chosenWidth < 8 || chosenWidth > 35)
            {
                msg6.setText("<html><div style=\"text-align:center;\">Niepoprawe wymiary<br>(wysokość: od 8 do 20, szerokość: od 8 do 35)</div></html>");
            }
            else if (chosenMines < 5 || chosenMines > 80)
            {
                msg6.setText("<html><div style=\"text-align:center;\">Niepoprawna liczba min<br>(od 5 do 80)</div></html>");
            }
            else
            {
                frame.dispose();
                GameManager.gameState = GameManager.GameState.PLAYING;
            }
        });

        JComboBox<String> box = new JComboBox<>(new String[]{"Łatwy", "Średni", "Trudny", "Własny"});
        box.setBounds(150, 170, 100, 20);
        box.addItemListener(e -> {
            if(box.getSelectedIndex() == 3)
            {
                widthField.setEditable(true);
                heightField.setEditable(true);
                mineField.setEditable(true);
            }
            else
            {
                chosenHeigth = levels.get(box.getSelectedIndex()).height;
                chosenWidth = levels.get(box.getSelectedIndex()).width;
                chosenMines = levels.get(box.getSelectedIndex()).mines;
                widthField.setText(String.valueOf(chosenWidth));
                heightField.setText(String.valueOf(chosenHeigth));
                mineField.setText(String.valueOf(chosenMines));
                widthField.setEditable(false);
                heightField.setEditable(false);
                mineField.setEditable(false);
            }
        });

        frame.setSize(400, 500);
        frame.setLayout(null);
        frame.add(title);
        frame.add(btn);
        frame.add(box);
        frame.add(msg1);
        frame.add(msg2);
        frame.add(msg3);
        frame.add(msg4);
        frame.add(msg5);
        frame.add(msg6);
        frame.add(widthField);
        frame.add(heightField);
        frame.add(mineField);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public int getChosenWidth()
    {
        return chosenWidth;
    }
    public int getChosenHeigth()
    {
        return chosenHeigth;
    }
    public int getChosenMines()
    {
        return chosenMines;
    }
    private static class Level
    {
        final int width;
        final int height;
        final int mines;
        public Level(int w, int h, int m)
        {
            this.height = h;
            this.width = w;
            this.mines = m;
        }
    }
}
