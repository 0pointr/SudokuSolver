/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author debd_admin
 */
public class Board {
    boolean[][] rowCheck;
    boolean[][] colCheck;
    boolean[][] gridCheck;
    
    Element[][] board;

    public Board(Element[][] board) {
        this.board = board;
        rowCheck = new boolean[9][9];
        colCheck = new boolean[9][9];
        gridCheck = new boolean[9][9];
        
        init2DArray(rowCheck);
        init2DArray(colCheck);
        init2DArray(gridCheck);
        preProcess();
        
        
        //System.out.format("%s ", Arrays.toString(board[4][3].domain));
    }
    
    public final void init2DArray (boolean[][] arr) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                    arr[i][j] = false;
            }
        }
    }
    
    private void preProcess () {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                int valIndex = board[i][j].currVal-1;
                if (valIndex >= 0) {
                    rowCheck[i][valIndex] = true;
                    colCheck[j][valIndex] = true;
                    gridCheck[getGridNo(i,j)][valIndex] = true;     // true means that value exists
                    
                    for (int k=0; k<9; k++)
                        board[i][k].domain[valIndex] = false;       // false means to delete this value from domain
                    for (int k=0; k<9; k++)
                        board[k][j].domain[valIndex] = false;
                    
                }
            }
        }
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
            board[i][j].setOrig(board[i][j].domain);
            }
        }
    }
    
    private int getGridNo(int r, int c) {
        r /= 3;
        c /= 3;
        return r*3+c;
    }
    
    private boolean checkConstraint (Element e) {
        int r = e.r;
        int c = e.c;
        int gridNo = getGridNo(r,c);
        boolean valid = !rowCheck[r][e.currVal-1] && !colCheck[c][e.currVal-1] && !gridCheck[gridNo][e.currVal-1];
        //System.out.format("row %s, col %s, grid %s\n", rowCheck[r][e.currVal-1],colCheck[c][e.currVal-1],gridCheck[gridNo][e.currVal-1]);
        /*
        if (valid) {
            rowCheck[r][e.currVal-1] = true;
            colCheck[c][e.currVal-1] = true;
            gridCheck[gridNo][e.currVal-1] = true;
        }
       */
        return valid;
    }
    
    private void reinitElement (Element e) {
        e.reset();
        e.currVal = 0;
        
        /*
        e.domain[e.currVal-1] = false;
        for (int i=0; i<9; i++) {
            if (rowCheck[e.r][i] == true)
                e.domain[i] = false;
        }
        for (int i=0; i<9; i++) {
            if (colCheck[i][e.c] == true)
                e.domain[i] = false;
        }
        int gridN = getGridNo(e.r, e.c);
        for (int i=0; i<9; i++) {
            if (gridCheck[gridN][i] == true)
                e.domain[i] = false;
        }
                */
    }
    
    private void updateSupportAdd (Element e, int oldVal) {
        int gridN = getGridNo(e.r, e.c);
        
        if (oldVal != 0) {
            rowCheck[e.r][oldVal-1] = false;
            colCheck[e.c][oldVal-1] = false;
            gridCheck[gridN][oldVal-1] = false;
        }
        
        rowCheck[e.r][e.currVal-1] = true;
        colCheck[e.c][e.currVal-1] = true;
        gridCheck[gridN][e.currVal-1] = true;
    }
    
    private void updateSupportDelete (Element e, int oldVal) {
        rowCheck[e.r][oldVal-1] = false;
        colCheck[e.c][oldVal-1] = false;
        int gridN = getGridNo(e.r, e.c);
        gridCheck[gridN][oldVal-1] = false;
    }
    
    public Element[][] fillBoard () {
        Stack<Element> stk = new Stack<>();
        int r = 0, c = 0;
        boolean lastPopd = false;
        
        stk.push(board[0][0]);
        
        while (!stk.empty()) {
            Element tmp = stk.peek();
            
            //if (tmp.r == 4 && tmp.c == 3)
                //System.out.println("here + " + Arrays.toString(tmp.domain));
            //System.out.format("%d %d: %d:\n", tmp.r, tmp.c, tmp.currVal);
            //System.out.println(Arrays.toString(tmp.domain));
            //System.out.println(Arrays.toString(tmp.orig));
            
            if (!tmp.fixed) {       // fixed are given vals
                int oldVal = tmp.currVal;
                
                if (tmp.setNextValue() == -1) {     // get next assignment
                    updateSupportDelete(tmp, oldVal);
                    reinitElement(tmp);
                    stk.pop();
                    lastPopd = true;
                    continue;
                }
                boolean breakFlag = false;
                while (checkConstraint(tmp) == false) {
                    if (tmp.setNextValue() == -1) {             // if domain becomes empty
                        reinitElement(tmp);
                        stk.pop();
                        breakFlag = true;
                        if (lastPopd)
                            updateSupportDelete(tmp, oldVal);
                        lastPopd = true;
                        break;
                    }
                }
                if (breakFlag) {
                    continue;
                }
                
                updateSupportAdd(tmp, oldVal);      // need to reset prev set value if any
                lastPopd = false;
            }
            else if (lastPopd == true) {
                stk.pop();
                continue;
            }
            r = tmp.r;
            c = tmp.c;
            if (r==8 && c==8) return board;
            if (r == 8)
                c = (c+1) % 9;
            r = (r + 1) % 9;
            
            stk.push(board[r][c]);
            //System.out.println("Looping..");
        }
        return null;  // no solution
    }
}
