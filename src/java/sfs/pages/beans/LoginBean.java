/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import sfs.catalog.CatalogServiceOption;
import sfs.persistence.objects.User;
import sfs.service.common.Service;
import static sfs.Utilities.*;
/**
 *
 * @author WL72166
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean extends CommonBean {

    private String username;  
    private String password;  
    private User loggedUser;

    public LoginBean() {
        super();
    }
    
    
    
    
      
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
  
    public void login() {  

        RequestContext context = RequestContext.getCurrentInstance();  
        boolean loggedIn = false;  
        FacesMessage msg = null;
          
        if(username == null || password == null) {  
            loggedIn =  false;
        }else{
            try{
                User usr = (User)Service.exec(null, CatalogServiceOption.GET_USER, username,password);
                if(usr == null){
                    msg = new FacesMessage(get("loginerror"),get("invaliduserorpassword"));
                }else{
                    loggedUser = usr;
                    loggedIn = true;
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", usr);
                    setUser(usr);
                    msg = new FacesMessage("",get("loginsuccess"));
                }
            }catch(Exception e){
                msg = new FacesMessage(get("generic_error"),e.getMessage());
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        context.addCallbackParam("loggedIn", loggedIn); 
    }
    
    public void logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        loggedUser = null;
    }
    
   
    

    public String getLoginButtonVisibility() {
        if(loggedUser==null){
            return "visible";
        }else{
            return "hidden";
        }
    }


    public String getLogoutButtonVisibility() {
        if(loggedUser==null){
            return "hidden";
        }else{
            return "visible";
        }
    }
    
    public String getLoggedUserName(){
        if(loggedUser==null){
            return "";
        }else{
            return loggedUser.getName();
        }
    }
    
    public String getIfLoggedInVisibility(){
        if(getUser()==null){
            return "hidden";
        }else{
            return "visible";
        }
    }

   
}
