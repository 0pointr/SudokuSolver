/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author debd_admin
 */
public class SudokuDemo {
    /*
    int[][] initVals = new int [][] {{7,5,8,9,2,3,1,4,6},
                                     {2,4,3,1,6,7,5,9,8},
                                     {1,9,6,4,5,8,7,2,3},
                                     {6,2,7,3,9,5,8,1,4},
                                     {8,1,5,7,4,6,2,3,9},
                                     {4,3,9,8,1,2,6,7,5},
                                     {3,7,1,5,8,4,9,6,2},
                                     {5,6,4,2,7,9,3,8,1},
                                     {9,8,2,6,3,1,4,5,7}};
    */
    /*int[][] initVals = new int [][] {{7,5,0,9,0,3,0,0,6},
                                     {0,0,0,0,0,0,0,0,0},
                                     {0,0,0,4,5,0,0,0,3},
                                     {6,2,0,0,9,0,8,0,0},
                                     {0,1,5,0,0,0,2,3,0},
                                     {0,0,9,0,1,0,0,7,5},
                                     {3,0,0,0,8,4,0,0,0},
                                     {0,0,0,0,0,0,0,0,0},
                                     {9,0,0,6,0,1,0,5,7}};*/
    //{{3, 0, 6, 5, 0, 8, 4, 0, 0},
    /*int[][] initVals = new int [][] {{7,5,0,9,0,3,1,4,6},
                                     {0,0,0,0,0,0,0,0,0},
                                     {1,0,0,4,5,0,0,0,3},
                                     {6,2,7,3,9,0,8,1,4},
                                     {0,0,5,7,4,6,2,3,9},
                                     {4,3,9,8,1,2,6,7,5},
                                     {3,7,1,5,8,4,9,6,2},
                                     {5,6,4,2,7,9,3,8,1},
                                     {9,0,0,6,3,1,4,5,7}};*/
    Board gameBoard;
    Element[][] grid;
    
    public SudokuDemo(int[][] initVals) {
        grid = new Element[9][9];
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                grid[i][j] = new Element(initVals[i][j], i, j, initVals[i][j]>0);
            }
        }
        gameBoard = new Board(grid);
    }
    
    public int[][] start () {
        if (gameBoard.fillBoard() == null) {
            System.out.println("No Solutions.");
            return null;
        }
        
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                System.out.format("%d ", grid[i][j].currVal);
            }
            System.out.println();
        }
        int[][] res = new int[9][9];
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                res[i][j] = grid[i][j].currVal;
            }
        }
        
        return res;
    }
    
    public static void main (String[] args) {
        //SudokuDemo skd = new SudokuDemo(initVals);
        // skd.start();
    }
}
