/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.swing.text.DateFormatter;
import org.primefaces.context.RequestContext;
import sfs.TextResourcesUtil;
import sfs.Utilities;
import sfs.persistence.objects.User;

/**
 *
 * @author WL72166
 */
public  abstract class CommonBean implements Serializable {

    private Properties lang ;
    private User user;
    private static String contextPath=null;
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    public CommonBean() {
        lang = TextResourcesUtil.getPropertiesObject();
        user = (User)getSessionObject("user");
    }
    
    
    public void setSessionObject(String name, Object obj){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(name, obj);
    }
    
    public Object getSessionObject(String name){
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
    }
    
    public Object getRequestValue(String name){
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }
    
    public void addCallbackParam(String name,Object value){
        RequestContext.getCurrentInstance().addCallbackParam(name, value);
    }
    
    public void addMessage(String shortDesc, String detail){
        String d1 = getLang().getProperty(shortDesc);
        String d2 = getLang().getProperty(detail);
        FacesMessage msg = new FacesMessage(d1!=null?d1:shortDesc, d2!=null?d2:detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    

    public Properties getLang() {
        return lang;
    }

    public void setLang(Properties lang) {
        this.lang = lang;
    }
    
    public void doForward(String redirect) {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        NavigationHandler myNav = facesContext.getApplication().getNavigationHandler();
//        myNav.handleNavigation(facesContext, null, redirect);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {
            ec.redirect(redirect);
        } catch (IOException ex) {
            Utilities.printToLog("Error al hacer el fordware", ex, Utilities.LOG_LEVEL_ERROR);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    public String getLoggedInVisibility(){
        if(getUser()==null){
            return "hidden";
        }
        return "visible";
    }
    
    public String getContextPath(){
        if(contextPath==null){
            ServletContext sc = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
            contextPath = sc.getRealPath("/");
        }
        return contextPath;
    }
    
    public String getDate(Date d){
        return format.format(d);
    }
    
    
    
    
    
}
