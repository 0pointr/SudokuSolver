/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sudoku.SudokuDemo;

/**
 *
 * @author debd_admin
 */
public class MainGUI extends JFrame {

    int[][] problemGrid;
    ArrayList<JTextField> inputs;
    Font resFont;
    
    public MainGUI() {
        this.resFont = new Font("Tahoma", Font.BOLD, 18);
        problemGrid = new int[9][9];
        initUI();
    }
    
    private void initUI() {
        setPreferredSize(new Dimension(480,400));
        setLayout(new BorderLayout());
        
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(9,9));
        
        inputs = new ArrayList<>();
        Font jtfFont = new Font("Tahoma", Font.PLAIN, 18);
        for (int i=0; i<81; i++) {
            JTextField jtf = new JTextField();
            jtf.setAlignmentX(JTextField.CENTER_ALIGNMENT);
            jtf.setAlignmentY(JTextField.CENTER_ALIGNMENT);
            jtf.setFont(jtfFont);
            inputs.add(jtf);
            grid.add(jtf);
        }
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(70,390));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JButton solve = new JButton("Solve");
        solve.addActionListener(solveAction);
        buttonPanel.add(solve);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JButton clear = new JButton("Clear");
        clear.addActionListener(clearAction);
        buttonPanel.add(clear);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JLabel note = new JLabel("<html>Note: Cells can <br>be left empty</html>");
        note.setFont(new Font("Tahoma", Font.PLAIN, 10));
        buttonPanel.add(note);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0,225)));
        JButton about = new JButton("About");
        about.addActionListener(aboutAction);
        buttonPanel.add(about);
        
        add(grid, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
        setTitle("SudokuSolver");
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    ActionListener solveAction = new ActionListener () {
        @Override
        public void actionPerformed(ActionEvent e) {
            solveFunction();
        }
    };

    private void solveFunction () {
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String thisInput = inputs.get(i * 9 + j).getText();
                problemGrid[i][j] = thisInput.isEmpty() ? 0 : Integer.parseInt(thisInput);
            }
        }
        SudokuDemo skd = new SudokuDemo(problemGrid);
        int[][] res = skd.start();

        if (res == null) {
            JOptionPane.showMessageDialog(this, "No Solutions!",
                    "Ding!",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField jtf = inputs.get(i * 9 + j);
                if (problemGrid[i][j] == 0) {
                    jtf.setForeground(Color.red);
                } else {
                    jtf.setForeground(Color.black);
                }
                jtf.setText(String.valueOf(res[i][j]));
            }
        }
    }
    
    ActionListener aboutAction = new ActionListener () {
    @Override
        public void actionPerformed(ActionEvent e) {
            aboutFunction();
        }
    };

    private void aboutFunction () {
        JOptionPane.showMessageDialog(this, "SudokuSolver\n"
                                             + "by DJ   (debd92@hotmail.com)",
                                               "About",
                                               JOptionPane.INFORMATION_MESSAGE);
    }
    
    ActionListener clearAction = new ActionListener () {
    @Override
        public void actionPerformed(ActionEvent e) {
            clearFunction();
        }
    };

    private void clearFunction () {
        for (JTextField jtf : inputs) {
            jtf.setText("");
        }
        repaint();
    }
    
    public static void main(String[] args) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGUI();
            }
        });
    }
}
