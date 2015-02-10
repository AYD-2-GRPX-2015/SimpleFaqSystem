/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs;

import java.io.Serializable;

/**
 *
 * @author wl72166
 */
public class TextPosition implements Serializable{

    private int start;
    private int end;
    private int length;
    private int linea;
    private String textContext;

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getTextContext() {
        return textContext;
    }

    public void setTextContext(String textContext) {
        this.textContext = textContext;
    }


    
    

    


}
