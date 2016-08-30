/*
 * Copyright (C) 2016 debd92 [@] hotmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sudoku;

import java.util.Stack;


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
    }
    
    public final void init2DArray (boolean[][] arr) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                    arr[i][j] = false;
            }
        }
    }
    
    // reduce the domains and init the support data structures
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
        // save a copy of the domain in current state
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
    
    // reinit a board element when deleted afte being assigned for once
    // happens during backtracking
    private void reinitElement (Element e) {
        e.reset();
        e.currVal = 0;
    }
    
    // make modifications to support structures in add event
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
    
    // make modifications to support structures in delete event
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
                
                if (tmp.setNextValue() == -1) {         // get next assignment
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
                        if (lastPopd)                           // supports were modified if an assignment occured in past
                            updateSupportDelete(tmp, oldVal);   // signified by if the op in the last iter on stack was a pop
                        lastPopd = true;
                        break;
                    }
                }
                if (breakFlag) {
                    continue;
                }
                
                updateSupportAdd(tmp, oldVal);
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
