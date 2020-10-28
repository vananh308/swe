/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gui.PuzzelGUI;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author vananh
 */
public class manager {
    
    int vananh = 1;
    PuzzelGUI viewPuzzel;
    int size = 3; // vì thấp nhất là 3
    int moveCount = 0;
    int elapseCount = 0;
    JButton[][] matrix;
    JButton[][] matrix1;
    runElapse r = new runElapse();
    // Assignment2 SWEeeeeeeee
    

    public manager(PuzzelGUI viewPuzzel) {
        this.viewPuzzel = viewPuzzel;
        viewPuzzel.setResizable(false);
        createButton(size);
        r.start();
        changeSize();
        newGame();
    }

    public void changeSize() {
        viewPuzzel.getSizeGame().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (viewPuzzel.getSizeGame().getSelectedIndex() == 0) {
                    int size = 3;
                    viewPuzzel.getPanelPuzzel().removeAll();
                    createButton(size);
                } else {
                    int size = 4;
                    viewPuzzel.getPanelPuzzel().removeAll();
                    createButton(size);
                }
            }
        });
    }

    public void createButton(int size) {
        this.size = size;
        int count = 0;
        // có hàng có cột là Gridlayout, khoảng cách btn là 10
        GridLayout grid = new GridLayout(size, size, 10, 10);
        viewPuzzel.getPanelPuzzel().setPreferredSize(new Dimension(size * 60, size * 60));//đẹp
        viewPuzzel.getPanelPuzzel().setLayout(grid);
        // dủng mảng hai chiều đẩy các btn lên, 2 vòng for lồng nhau
        matrix = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                count++;
                JButton bt = new JButton(count + "");
                matrix[i][j] = bt; // add button vào matrix để điều khiển cho dễ
                viewPuzzel.getPanelPuzzel().add(bt);
                //add action to all button
                bt.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        if (checkMoveButton(bt)) {
                            moveButton(bt);
                            moveCount++;
                            viewPuzzel.getLblMove().setText("Move count: " + moveCount);
                            checkWin();
                        }
                    }
                });
            }
        }
        matrix[size - 1][size - 1].setText(""); //btn cuối cùng
        viewPuzzel.pack(); // frame co dãn đúng với panel
        mixButton();
    }

    // Point return tọa độ của btn ý, tìm thằng rỗng để đổi với btn bên cạnh
    public Point getButtonEmpty() {
        int i = 0, j = 0;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (matrix[i][j].getText().equals("")) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public void mixButton() {
        for (int k = 0; k < 1000; k++) {
            Point p = getButtonEmpty(); //lấy tọa độ rỗng
            int i = p.x;
            int j = p.y;
            Random rd = new Random();
            int valueRandom = rd.nextInt(4); //0-1-2-3
            switch (valueRandom){
                case 0: {//up
                    if (i > 0) {
                        matrix[i][j].setText(matrix[i - 1][j].getText());
                        matrix[i][j].setText("");
                        break;
                    }
                }
                case 1: {//down
                    if (i < size - 1) {
                        matrix[i][j].setText(matrix[i + 1][j].getText());
                        matrix[i][j].setText("");
                        break;
                    }
                }
                case 2:{ //left
                    if (j > 0) {
                        matrix[i][j].setText(matrix[i][j - 1].getText());
                        matrix[i][j].setText("");
                        break;
                    }
                }
                case 3: {//right
                    if (j < size - 1) {
                        matrix[i][j].setText(matrix[i][j + 1].getText());
                        matrix[i][j].setText("");
                          break;
                    }
                }
            }
        }
    }

    public boolean checkMoveButton(JButton btn) {
        int i1 = 0, j1 = 0;
        int x = getButtonEmpty().x;
        int y = getButtonEmpty().y;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (btn.getText().equals(matrix[i][j].getText())) {
                    i1 = i;
                    j1 = j;
                    break;
                }
            }
        }
        if ((x == i1 && ((j1 - 1) == y)) || (x == i1 && ((j1 + 1) == y))) {
            return true;
        }
        if ((y == j1 && (i1 - 1 == x)) || (y == j1 && (i1 + 1 == x))) {
            return true;
        }
        return false;
    }

    public void moveButton(JButton btn) {
        int i = getButtonEmpty().x;
        int j = getButtonEmpty().y;
        matrix[i][j].setText(btn.getText());
        btn.setText("");
    }

    //win khi tat ca btn giong nhu minh khoi tao ban dau
    public void checkWin() {
        boolean check = true;
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                count++;
                if (!matrix[i][j].getText().equals(count + "")) {
                    System.out.println("");
                    check = false;
                }
                if (count == (size * size - 1)) {
                    break;
                }
            }
        }
        if (check) {
            r.stop();
            JOptionPane.showMessageDialog(viewPuzzel, "You Won!!");
        }
    }

    public void newGame() {
        viewPuzzel.getBtnNewGame().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                viewPuzzel.getPanelPuzzel().removeAll();
                createButton(size);
                moveCount = 0;
                viewPuzzel.getLblMove().setText("Move count: 0");
                elapseCount = 0;
            }
        });
    }

    class runElapse extends Thread {
        @Override
        public void run() {
            while (true) {
                viewPuzzel.getLblElap().setText("Elapse : " + elapseCount + "(sec)");
                elapseCount++;
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
