/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.Arrays;

/**
 *
 * @author debd_admin
 */
public class Element {
    boolean[] domain;
    boolean[] orig;
    boolean fixed;
    int currVal;
    final int r, c;

    public Element(int currVal, int r, int c, boolean fixed) {
        this.currVal = currVal;
        this.fixed = fixed;
        this.r = r;
        this.c = c;
        
        this.domain = new boolean[9];
        for (int i=0; i<9; i++) {
            if (i != currVal-1)
                domain[i] = true;   //index+1 s are the values.
            else
                domain[i] = false;
        }
    }
    
    public void setOrig (boolean[] arr) {
        orig = Arrays.copyOf(arr, arr.length);
    }
    
    public void reset () {
        System.arraycopy(orig, 0, domain, 0, 9);
    }
    
    public int setNextValue () {
        if (currVal == 0)
            currVal = 1;
        for (int i=currVal-1; i<9; i++)
            if (domain[i] == true) {
                currVal = i+1;
                domain[i] = false;
                return currVal;
            }
        return -1;      // means domain is empty
    }
    
}
