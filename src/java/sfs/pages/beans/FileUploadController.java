/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;

import java.io.File;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author WL72166
 */
@ManagedBean(name = "fileUploadController")
@RequestScoped
public class FileUploadController {

    private Part file;
    
    /** Creates a new instance of FileUploadController */
    public FileUploadController() {
        System.out.println("aaaa");
    }
    
        
 
    public Part getFile() {
        return file;
    }
 
    public void setFile(Part file) {
        this.file = file;
    }
 
    public void upload() {
        if(file != null) {
            FacesMessage msg = new FacesMessage("Succesful", file.getName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    } 

}
