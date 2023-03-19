import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameWindow extends Thread
{
    private static final ArrayList<ImageIcon> icons = new ArrayList<>();
    private final ArrayList<ArrayList<Element>> fields = new ArrayList<>();
    private final int width;
    private final int height;
    private final int mines;
    private boolean firstAttempt;
    private boolean canClick;
    private final ArrayList<Color> colors;
    private boolean win;
    JFrame frame;

    static
    {
        icons.add(new ImageIcon(new ImageIcon("Icons/mine.jpg").getImage().getScaledInstance(35,35,Image.SCALE_DEFAULT)));
        icons.add(new ImageIcon(new ImageIcon("Icons/flag.png").getImage().getScaledInstance(35,35,Image.SCALE_DEFAULT)));
    }

    public GameWindow(int width, int height, int mines)
    {
        this.width = width;
        this.height = height;
        this.mines = mines;
        this.firstAttempt = true;
        this.canClick = true;
        this.win = false;
        this.colors = new ArrayList<>(Arrays.asList(Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.MAGENTA, Color.CYAN,Color.PINK, Color.BLACK));
    }

    private void generateMines(int x, int y)
    {
        ArrayList<Integer> minePlaces = new ArrayList<>();
        int num = (width*height)*mines/100;
        int m = 0;
        while(num > 0)
        {
            int rand = (int) Math.floor(Math.random()*(width*height - 1));
            if (!minePlaces.contains(rand) && (Math.abs(rand%width - x) > 1 || Math.abs(rand/width - y) > 1))
            {
                minePlaces.add(rand);
                num--;
            }
        }
        for (int i=0; i<height; i++)
        {
            ArrayList<Element> tmp = new ArrayList<>();
            for (int j=0; j<width; j++)
            {
                boolean check = minePlaces.contains(m);
                tmp.add(new Element(check));
                m++;
            }
            fields.add(tmp);
        }
        for (int i=0; i<height; i++)
        {
            for (int j=0; j<width; j++)
            {
                if (!fields.get(i).get(j).isMine)
                {
                    int rowStart  = Math.max(j - 1, 0);
                    int rowFinish = Math.min(j + 1, width - 1);
                    int colStart  = Math.max(i - 1, 0);
                    int colFinish = Math.min(i + 1, height - 1);
                    for (int curCol=colStart; curCol<=colFinish; curCol++)
                    {
                        for (int curRow=rowStart; curRow<=rowFinish; curRow++)
                        {
                            if (fields.get(curCol).get(curRow).isMine)
                            {
                                fields.get(i).get(j).mines = fields.get(i).get(j).mines + 1;
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateButtons()
    {
        ArrayList<ArrayList<JButton>> buttons = new ArrayList<>();
        frame.setLayout(new GridLayout(height,width));
        for (int i=0; i<height; i++)
        {
            ArrayList<JButton> tmp = new ArrayList<>();
            for (int j=0; j<width; j++)
            {
                JButton btn = new JButton();
                int k = j, r = i;
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (canClick)
                        {
                            if (e.getButton() == 3) {
                                if (fields.get(r).get(k).state == ElementState.FLAGGED) {
                                    btn.setIcon(null);
                                    fields.get(r).get(k).state = ElementState.HIDDEN;
                                } else if (fields.get(r).get(k).state == ElementState.HIDDEN) {
                                    btn.setIcon(icons.get(1));
                                    fields.get(r).get(k).state = ElementState.FLAGGED;
                                    int flagged = 0;
                                    for (int w = 0; w < height; w++) {
                                        for (int l = 0; l < width; l++) {
                                            if (fields.get(w).get(l).isMine && fields.get(w).get(l).state==ElementState.FLAGGED) {
                                                flagged++;
                                            }
                                        }
                                    }
                                    if(flagged==(width*height)*mines/100)
                                    {
                                        win=true;
                                        GameManager.gameState = GameManager.GameState.RESULTS;
                                    }
                                }
                            } else {
                                btn.setIcon(null);
                                if (firstAttempt) {
                                    generateMines(k, r);
                                    firstAttempt = false;
                                }
                                if (fields.get(r).get(k).isMine) {
                                    btn.setIcon(icons.get(0));
                                    canClick = false;
                                    for (int w = 0; w < height; w++) {
                                        for (int l = 0; l < width; l++) {
                                            if (fields.get(w).get(l).isMine) {
                                                buttons.get(w).get(l).setIcon(icons.get(0));
                                            }
                                        }
                                    }
                                    GameManager.gameState = GameManager.GameState.RESULTS;
                                } else {
                                    btn.setBackground(Color.WHITE);
                                    if (fields.get(r).get(k).mines != 0) {
                                        btn.setText(String.valueOf(fields.get(r).get(k).mines));
                                        btn.setFont(new Font("Arial", Font.PLAIN, 17));
                                        btn.setForeground(colors.get(fields.get(r).get(k).mines - 1));
                                    } else {
                                        showElement(r, k, buttons);
                                    }
                                    fields.get(r).get(k).state = ElementState.SHOWN;
                                }
                            }
                        }
                    }
                });
                btn.setSize(45,45);
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setOpaque(true);
                frame.add(btn);
                tmp.add(btn);
            }
            buttons.add(tmp);
        }
    }

    private void showElement(int x, int y, ArrayList<ArrayList<JButton>> buttons)
    {
        if(fields.get(x).get(y).mines == 0)
        {
            int rowStart  = Math.max(y - 1, 0);
            int rowFinish = Math.min(y + 1, width - 1);
            int colStart  = Math.max(x - 1, 0);
            int colFinish = Math.min(x + 1, height - 1);
            for (int curCol=colStart; curCol<=colFinish; curCol++)
            {
                for (int curRow=rowStart; curRow<=rowFinish; curRow++)
                {
                    if((curCol!=x || curRow!=y) && fields.get(curCol).get(curRow).state == ElementState.HIDDEN)
                    {
                        buttons.get(curCol).get(curRow).setIcon(null);
                        fields.get(curCol).get(curRow).state = ElementState.SHOWN;
                        Element.shownElementsCount++;
                        buttons.get(curCol).get(curRow).setBackground(Color.WHITE);
                        if (fields.get(curCol).get(curRow).mines == 0) {
                            showElement(curCol, curRow, buttons);
                        } else {
                            buttons.get(curCol).get(curRow).setText(String.valueOf(fields.get(curCol).get(curRow).mines));
                            buttons.get(curCol).get(curRow).setFont(new Font("Arial", Font.PLAIN, 17));
                            buttons.get(curCol).get(curRow).setForeground(colors.get(fields.get(curCol).get(curRow).mines - 1));
                        }
                    }
                }
            }
        }
    }
    public boolean gameWin()
    {
        return win;
    }
    @Override
    public void run()
    {
        frame = new JFrame();
        generateButtons();
        frame.setSize(45*width, 45*height);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    private static class Element
    {
        ElementState state;
        int mines;
        final boolean isMine;
        static int shownElementsCount;
        static
        {
            shownElementsCount = 0;
        }
        public Element(boolean isMine)
        {
            this.isMine = isMine;
            this.state = ElementState.HIDDEN;
            this.mines = 0;
        }
    }
    private enum ElementState
    {
        HIDDEN, SHOWN, FLAGGED
    }
}
