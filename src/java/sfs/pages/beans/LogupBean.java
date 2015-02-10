/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;



import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import static sfs.Utilities.get;
import sfs.catalog.CatalogServiceOption;
import sfs.catalog.EntidadEstado;
import sfs.persistence.objects.User;
import sfs.service.UserAdminService;
import sfs.service.common.Service;
import sfs.service.common.ServiceFactory;

/**
 *
 * @author WL72166
 */
@ManagedBean(name = "logupBean")
@RequestScoped
public class LogupBean extends CommonBean{

    private String luUsername;
    private String luPassword;
    private String luPasswordconfirm;
    private String luName;
    private String luEmail;
    
    /**
     * Creates a new instance of LogupBean
     */
    public LogupBean(){
    }

    public String getLuUsername() {
        return luUsername;
    }

    public void setLuUsername(String luUsername) {
        this.luUsername = luUsername;
    }

    public String getLuPassword() {
        return luPassword;
    }

    public void setLuPassword(String luPassword) {
        this.luPassword = luPassword;
    }

    public String getLuPasswordconfirm() {
        return luPasswordconfirm;
    }

    public void setLuPasswordconfirm(String luPasswordconfirm) {
        this.luPasswordconfirm = luPasswordconfirm;
    }

    public String getLuName() {
        return luName;
    }

    public void setLuName(String luName) {
        this.luName = luName;
    }

    public String getLuEmail() {
        return luEmail;
    }

    public void setLuEmail(String luEmail) {
        this.luEmail = luEmail;
    }

    
    
    public void logup(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            User usr = new User();
            usr.setUser(luUsername);
            usr.setEstado(EntidadEstado.ACTIVO);
            usr.setMail(luEmail);
            usr.setName(luName);
            usr.setPassword(luPassword);
            Service.exec(null, CatalogServiceOption.ADD_USER, usr,luPasswordconfirm);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(get("logup"),get("creacion_usuario_exitosa")));
        } catch (Exception e) {
            context.addCallbackParam("success", false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(get("generic_error"),e.getMessage()));
        }
    }

    
}
