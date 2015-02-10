/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sfs.TextResourcesUtil;
import sfs.Utilities;
import sfs.catalog.CatalogServiceOption;
import sfs.catalog.EntidadEstado;
import sfs.persistence.objects.Faq;
import sfs.persistence.objects.FaqContact;
import sfs.persistence.objects.FaqTopic;
import sfs.persistence.objects.UploadedFile;
import sfs.persistence.objects.User;
import sfs.service.common.Service;


/**
 *
 * @author WL72166
 */
@ManagedBean(name = "faqAbmBean")
@RequestScoped
public class FaqAbmBean extends ContactAbmBean{

    private String title;
    private String shortDescription;
    private String longDescription;
    private Integer faqid;
    private Integer topicid;
    private String topicText;
    
    //ABM ARCHIVOS
    private Part file;
    private List<UploadedFile> uploadedFiles;
    private String serializedUploadedFiles;
    
    
    //ABM CONTACTOS DEL FAQ
    private String contactName;
    private String contactPhone1;
    private String contactPhone2;
    private String contactEmail1;
    private String contactEmail2;
    private String contactArea;
    private List<FaqContact> faqContacts;
    private String serializedContacts;
    
    
    /**
     * Creates a new instance of FaqAbmBean
     */
    public FaqAbmBean(){
        super();
        
        if (getUser() == null) {
            doForward("index.xhtml");
        } else {

            String sfaqid = (String) getRequestValue("faqid");
            String option = (String) getRequestValue("opt");

            if (option==null || !option.equals("new")) {

                if (sfaqid != null) {
                    try {
                        faqid = new Integer(sfaqid);
                    } catch (NumberFormatException e) {
                        addMessage("generic_error", e.getMessage());
                    }
                }

                if (faqid != null) {
                    try {
                        Faq currentFaq = (Faq) Service.exec(getUser(), CatalogServiceOption.GET_FAQ, faqid);
                        title = currentFaq.getTitle();
                        shortDescription = currentFaq.getShortDescription();
                        longDescription = currentFaq.getLongDescription();
                        topicid = currentFaq.getTopic().getId();
                        topicText = currentFaq.getTopic().getName();
                        uploadedFiles = currentFaq.getUploadedFileList();
                        setFaqContacts(currentFaq.getFaqContactList());
                    } catch (Exception e) {
                        addMessage("generic_error", e.getMessage());
                    }
                }
            }else{
                
            }
            
        }
    }
    


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Integer getFaqid() {
        return faqid;
    }

    public void setFaqid(Integer faqid) {
        //if(faqid==0) faqid = null;
        this.faqid = faqid;
    }

    public Integer getTopicid() {
        return topicid;
    }

    public void setTopicid(Integer topicid) {
        this.topicid = topicid;
    }

    public String getTopicText() {
        return topicText;
    }

    public void setTopicText(String topicText) {
        this.topicText = topicText;
    }
    
    public String getFormTitle(){
        if(faqid==null){
            return getLang().getProperty("newfaq");
        }
        return getLang().getProperty("modifyfaq")+ " *[Id: "+faqid+"]*";
    }

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }
    
    public String getSerializedUploadedFiles(){
        serializedUploadedFiles = serializeUploadedFiles(uploadedFiles);
        return serializedUploadedFiles;
    }
    
    public void setSerializedUploadedFiles(String str){
        this.serializedUploadedFiles = str;
        this.uploadedFiles = unserializeUploadedFiles(str);
    }
    
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    public String getDeleteButtonVisibility(){
        if(faqid==null){
            return "hidden";
        }
        return "visible";
    }
    
    
    public void actionListener(){
        if(faqid==null){
            addFaq();
        }else{
            modifyFaq();
        }
    }
    
    public void addFaq(){
        try {
            
            if (getUser() == null) {
                addMessage("generic_error", "youmustbeloggedin");
                return;
            }
            
            if(topicid==null){
                addMessage("settopic", "settopic");
                return;
            }
            
            System.out.println("ADDINGFAQ 1");
            
            Faq currentFaq = new Faq();
            currentFaq.setEstado(EntidadEstado.ACTIVO);
            currentFaq.setLongDescription(longDescription);
            currentFaq.setShortDescription(shortDescription);
            currentFaq.setPostdate(new Date());
            currentFaq.setTitle(title);
            currentFaq.setVisits(0);
            currentFaq.setUser(getUser());
            FaqTopic ft = (FaqTopic)Service.exec(getUser(), CatalogServiceOption.GET_TOPIC, topicid);
            System.out.println("ADDINGFAQ 2");
            currentFaq.setTopic(ft);
            Service.exec(getUser(), CatalogServiceOption.ADD_FAQ, currentFaq);
            System.out.println("ADDINGFAQ 3");
            addUploadedFiles(currentFaq, getUser(), uploadedFiles);
            System.out.println("ADDINGFAQ 4");
            addContactsToFaq(currentFaq);
            System.out.println("ADDINGFAQ 5");
            faqid=currentFaq.getId();
            topicid=ft.getId();
            topicText = ft.getName();
            addMessage("faqsuccesscreation", "faqsuccesscreation");
            doForward("index.xhtml?p=faqviewform&faq="+currentFaq.getId());
        } catch (Exception e) {
            addMessage("generic_error", e.getMessage());
            Utilities.printToLog("Error al crear el faq", e, Utilities.LOG_LEVEL_ERROR);
        }
    }
    
    public void modifyFaq(){
        
        try {
            
            if (getUser() == null) {
                addMessage("generic_error", "youmustbeloggedin");
                return;
            }
            
            Faq currentFaq = (Faq)Service.exec(getUser(), CatalogServiceOption.GET_FAQ, faqid);
            
            
            FaqTopic ft = (FaqTopic)Service.exec(getUser(), CatalogServiceOption.GET_TOPIC, topicid);
            currentFaq.setTopic(ft);
            
            currentFaq.setEstado(EntidadEstado.ACTIVO);
            currentFaq.setLongDescription(longDescription);
            currentFaq.setShortDescription(shortDescription);
            currentFaq.setTitle(title);
            currentFaq.setTopic(ft);
            Service.exec(getUser(), CatalogServiceOption.MODIFY_FAQ, currentFaq);
            addUploadedFiles(currentFaq, getUser(), uploadedFiles);
            addContactsToFaq(currentFaq);
            addMessage("faqsuccessmodification", "faqsuccessmodification");
            doForward("index.xhtml?p=faqviewform&faq="+currentFaq.getId());
        } catch (Exception e) {
            addMessage("generic_error", e.getMessage());
            Utilities.printToLog("Error al modificar el FAQ", e, Utilities.LOG_LEVEL_ERROR);
        }
    }
    
    public void deleteFaq(){
        try {
            
            if (getUser() == null) {
                addMessage("generic_error", "youmustbeloggedin");
                return;
            }
            
            Faq currentFaq = (Faq)Service.exec(getUser(), CatalogServiceOption.GET_FAQ, faqid);
            currentFaq.setEstado(EntidadEstado.ELIMINADO);
            Service.exec(getUser(), CatalogServiceOption.MODIFY_FAQ, currentFaq);
            addMessage("faqsuccessdelet", "faqsuccessdelet");
            doForward("index.xhtml");
        } catch (Exception e) {
            addMessage("generic_error", e.getMessage());
        }
    }
    
    
    private void addUploadedFiles(Faq faq, User user, List<UploadedFile> ufs) throws Exception{
        if(ufs==null || ufs.isEmpty()) return;
        for(UploadedFile uf:ufs){
            
            if(uf.getId()!=null) continue;
            
            uf.setFaq(faq);
            uf.setUser(user);
            uf.setUpladDate(new Date());
            Service.exec(getUser(), CatalogServiceOption.ADD_UPLOADED_FILE, uf);
        }
    }
    
    public void upload() {  
        
        FileItem file = null;
        
        try{
            HttpServletRequest servletRequest =  (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(servletRequest);
            if(items!=null && !items.isEmpty()){
                file = items.get(0);
            }
            
        }catch(Exception ex){
            Utilities.printToLog("Error al cargar el archivo", ex, Utilities.LOG_LEVEL_ERROR);
        }
        
        
        if(file != null) {
            
            if(uploadedFiles == null){
                uploadedFiles = new ArrayList<UploadedFile>();
            }
            
            try {
                int r = (int)(Math.random() * 100000);
                
                String finalName = r + "_" + file.getName();
                String fullPath = TextResourcesUtil.getText("uploadpath") + File.separator + finalName;
                String uri =  "./sfile/" + finalName;
                File f = new File(fullPath);
                Utilities.copyData(file.getInputStream(), new FileOutputStream(f));

                UploadedFile uf = new UploadedFile();
                uf.setDescription(file.getName());
                uf.setFilesize((int) file.getSize());
                uf.setName(file.getName());
                uf.setPath(uri);
                uploadedFiles.add(uf);
                FacesMessage msg = new FacesMessage("Succesful", file.getName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Exception e) {
                addMessage("Error " + file.getName() + " not uploaded.", e.getMessage());
            }            
        }
    }
    
    
    private String serializeUploadedFiles(List<UploadedFile> ufaqs){
        
        if(ufaqs==null || ufaqs.isEmpty()){
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for(UploadedFile ufaq: ufaqs){
            sb.append(ufaq.getId());
            sb.append("|");
            sb.append(ufaq.getDescription());
            sb.append("|");
            sb.append(ufaq.getName());
            sb.append("|");
            sb.append(ufaq.getPath());
            sb.append("|");
            sb.append(ufaq.getFilesize());
            sb.append("||");
        }
        return sb.toString();
    }
    
    private List<UploadedFile> unserializeUploadedFiles(String str){
        if(str == null || str.isEmpty()){
            return null;
        }
        
        String[] s1 = str.split("[|][|]");
        
        if(s1==null || s1.length == 0){
            return null;
        }
        
        List<UploadedFile> files = new ArrayList<UploadedFile>();
        
        for(String s:s1){
            if(s.trim().equals("")) continue;
            String d[] = s.split("[|]");
            UploadedFile uf = new UploadedFile();
            
            if(d[0].trim().equals("null")){
                uf.setId(null);
            }else{
                uf.setId(Integer.parseInt(d[0]));
            }
            
            uf.setDescription(d[1]);
            uf.setName(d[2]);
            uf.setPath(d[3]);
            uf.setFilesize(Integer.parseInt(d[4]));
            files.add(uf);
        }
        return files;
    }
    
    
    //METODOS ESPECIFICOS PARA
    //ABM DE CONTACTOS DE FAQ
      
    public String getSerializedContacts() {
        serializedContacts = "";
        if(faqContacts!=null){
            for(FaqContact f:faqContacts){
                serializedContacts += f.getId()+";;"+
                                      getNullIfEmpty( f.getName()   )+";;"+
                                      getNullIfEmpty( f.getPhone1() )+";;"+
                                      getNullIfEmpty( f.getPhone2() )+";;"+
                                      getNullIfEmpty( f.getEmail1() )+";;"+
                                      getNullIfEmpty( f.getEmail2() )+";;"+
                                      getNullIfEmpty( f.getArea()   )+"||";
            }
        }
        return serializedContacts;
    }

    public void setSerializedContacts(String serializedContacts) {
        
        System.out.println("CONTACTOS SERIALIZADOS: " + serializedContacts);
        
        this.serializedContacts = serializedContacts;
        
        if(serializedContacts == null || serializedContacts.isEmpty()){
            faqContacts = null;
        }
        
        String data[] = serializedContacts.split("[|][|]");
        faqContacts = new ArrayList<>();
        for(String s:data){
            if(s.trim().equals("")) continue;
            String d[] = s.split(";;");
            FaqContact fc = new FaqContact();
            fc.setId(Integer.parseInt(d[0].trim()));
            if(!d[1].trim().equals("null")) fc.setName(d[1].trim());
            if(!d[2].trim().equals("null")) fc.setPhone1(d[2].trim());
            if(!d[3].trim().equals("null")) fc.setPhone2(d[3].trim());
            if(!d[4].trim().equals("null")) fc.setEmail1(d[4].trim());
            if(!d[5].trim().equals("null")) fc.setEmail2(d[5].trim());
            if(!d[6].trim().equals("null")) fc.setArea(d[6].trim());
            faqContacts.add(fc);
        }
        this.serializedContacts = serializedContacts;
        System.out.println("CONTACTOS DESERIALIZADOS CON EXITO:"+faqContacts.size());
    }
    
    public void addFaqContact(){
        try {
            
            if(faqContacts==null){
                faqContacts = new ArrayList<>();
            }
            
            FaqContact faqc = new FaqContact();
            faqc.setName(contactName);
            if(contactPhone1 != null && !contactPhone1.trim().equals("")) faqc.setPhone1(contactPhone1);
            if(contactPhone2 != null && !contactPhone2.trim().equals("")) faqc.setPhone2(contactPhone2);
            if(contactEmail1 != null && !contactEmail1.trim().equals("")) faqc.setEmail1(contactEmail1);
            if(contactEmail2 != null && !contactEmail2.trim().equals("")) faqc.setEmail2(contactEmail2);
            if(contactArea   != null && !contactEmail1.trim().equals("")) faqc.setArea(contactArea);
            faqc.setId(-1*(faqContacts.size()+1));
            
            faqContacts.add(faqc);
            
        } catch (Exception e) {
            Utilities.printToLog("Error al agregar el contacto al listado", e, Utilities.LOG_LEVEL_ERROR);
            addMessage("generic_error", e.getMessage());
        }
    }
    
    public void deleteFaqContact(FaqContact fc){
        try {
            
            if(faqContacts==null) return;
            
            if (fc == null) {
                addMessage("generic_error", "No selected Contact");
                return;
            }
            
            List<FaqContact> newList = new ArrayList<>();
            
            for(FaqContact f:faqContacts){
                if(!Objects.equals(f.getId(), fc.getId())){
                    if(f.getId()>=0){
                        newList.add(f);
                    }else{
                        f.setId(-1*(newList.size()+1));
                        newList.add(f);
                    }
                }else{
                    if(f.getId()>=0){
                        Service.exec(getUser(), CatalogServiceOption.DELETE_FAQ_CONTACT, f);
                    }
                }
            }
            
            faqContacts = newList;
            
        } catch (Exception e) {
            addMessage("generic_error", e.getMessage());
            Utilities.printToLog("Error al eliminar el contacto al listado", e, Utilities.LOG_LEVEL_ERROR);
        }
    }
    
    protected void addContactsToFaq(Faq f) throws Exception{
        if(f==null || faqContacts == null) return;
        for(FaqContact fc : faqContacts){
            System.out.println("AGREGANDO CONTACTO A FAQ:"+fc);
            if(fc.getId()>=0) continue;
            fc.setFaq(f);
            fc.setId(null);
            Service.exec(getUser(), CatalogServiceOption.ADD_FAQ_CONTACT, fc);
        }
    }
    
    public void deleteAction(FaqContact fc){
        deleteFaqContact(fc);
    }

    public String getContactName() {
        return contactName;
    }

    public List<FaqContact> getFaqContacts() {
        return faqContacts;
    }

    public void setFaqContacts(List<FaqContact> _faqContacts) {
        faqContacts = _faqContacts;
    }
    
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone1() {
        return contactPhone1;
    }

    public void setContactPhone1(String contactPhone1) {
        this.contactPhone1 = contactPhone1;
    }

    public String getContactPhone2() {
        return contactPhone2;
    }

    public void setContactPhone2(String contactPhone2) {
        this.contactPhone2 = contactPhone2;
    }

    public String getContactEmail1() {
        return contactEmail1;
    }

    public void setContactEmail1(String contactEmail1) {
        this.contactEmail1 = contactEmail1;
    }

    public String getContactEmail2() {
        return contactEmail2;
    }

    public void setContactEmail2(String contactEmail2) {
        this.contactEmail2 = contactEmail2;
    }

    public String getContactArea() {
        return contactArea;
    }

    public void setContactArea(String contactArea) {
        this.contactArea = contactArea;
    }
    
    private String getNullIfEmpty(String str){
        if(str==null || str.isEmpty()){
            return "null";
        }
        return str;
    }

   
    
}
