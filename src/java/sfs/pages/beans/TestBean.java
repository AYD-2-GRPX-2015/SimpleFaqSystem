/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;



/**
 *
 * @author WL72166
 */
@ManagedBean(name = "testBean")
@RequestScoped
public class TestBean {
    
    private String dato;

    /**
     * Creates a new instance of TestBean
     */
    public TestBean() {    
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }
    
    public void doSomthing(){
        System.out.println("dato:"+dato);
    }
    
}
