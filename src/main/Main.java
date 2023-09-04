package main;

import javax.swing.*;
import java.awt.*;

public class Main {

    public final static boolean ai_verses_ai = false;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.gray);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(888, 888));
        frame.setLocationRelativeTo(null);
//        frame.setUndecorated(true);

        Board board = new Board();

        Table table = new Table(board);
        table.add(board);
        frame.add(table);

        board.revalidate();
        board.repaint();

        frame.setVisible(true);

        if (ai_verses_ai)
            board.ai.aiMove();



//        System.out.println(board.ai.moveGenerationTest(5));


        // default
        // 1 =  20
        // 2 =  400
        // 3 =  8902
        // 4 =  197281
        // 5 =  4865609 // 258 off --- 351
        // 6 =  119060324
        // 7 =  3195901860

        // custom fen
        // 1 =  44
        // 2 =  1486
        // 3 =  62379
        // 4 =  2103487
        // 5 =  89941194

        if (!ai_verses_ai) {
            board.addListeners();
        }
        board.repaint();

    }
}






















