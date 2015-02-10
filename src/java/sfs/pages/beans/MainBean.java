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
@ManagedBean(name = "mainBean")
@RequestScoped
public class MainBean  extends CommonBean{

    private String requestedOption;

    public MainBean() {
        
        String p = (String)getRequestValue("p");
        requestedOption=p!=null? p:"welcome";   
    }

    public String getRequestedOption() {
        
        if(requestedOption == null){
            return "pages/welcome.xhtml";
        }else{
            return "pages/"+requestedOption+".xhtml";
        }
    }

    public void setRequestedOption(String requestedOption) {
        this.requestedOption = requestedOption;
    }
    
}
