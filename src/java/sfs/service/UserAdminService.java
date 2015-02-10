/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import sfs.Utilities;
import sfs.service.common.Service;
import sfs.service.common.ServiceOptionAnnotation;
import static sfs.Utilities.*;
import sfs.catalog.CatalogServiceOption;
import sfs.catalog.EntidadEstado;
import sfs.persistence.objects.User;
import sfs.persistence.objects.UserPermission;
import sfs.service.common.ServiceFactory;
import sfs.service.objects.ServiceOption;

/**
 *
 * @author WL72166
 */
public class UserAdminService extends Service{
    

    @ServiceOptionAnnotation
    public User getUser(String user, String password) throws Exception{

        EntityManager em = getEntityManager();
        User fuser = em.find(User.class,user);
        
        if(fuser==null){
            return null;
        }
        
        String md5Pass = Utilities.strToMd5(password);
        if(fuser.getPassword().equals(md5Pass)){
            return fuser;
        }
        return null;
    }
    
    @ServiceOptionAnnotation
    public List<UserPermission> getPermissionList(User user) throws IOException{
        
        List<ServiceOption> list = ServiceFactory.getServiceOptions();
        List<UserPermission> resp = new ArrayList<UserPermission>();
        
        for(ServiceOption so : list){
            UserPermission up = new UserPermission();
            up.setServiceOption(so.getServiceOption());
            resp.add(up);
        }
        return resp;
    }
    
    
    @ServiceOptionAnnotation(transaction = true)
    public void addUser(User usr,String passwordConfirm) throws Exception{
        
        if(usr.getUser() == null || usr.getUser().trim().equals("")){
            throw new Exception(get("setusername"));
        }
        
        if(usr.getName() == null || usr.getName().trim().equals("")){
            throw new Exception(get("setname"));
        }
        
        if(usr.getMail() == null || usr.getMail().trim().equals("")){
            throw new Exception(get("setemail"));
        }
        
        if(usr.getPassword() == null || usr.getPassword().trim().equals("")){
            throw new Exception(get("setpassword"));
        }
        
        if(passwordConfirm!=null && !passwordConfirm.equals(usr.getPassword())){
            throw new Exception(get("passnomatch"));
        }
        
        User exist = getEntityManager().find(User.class, usr.getUser());
        
        if(exist!=null){
            throw new Exception(get("useralreadyexists"));
        }
        
        if(usr.getEstado()== null){
            usr.setEstado(EntidadEstado.ACTIVO);
        }
        
        usr.setPassword(Utilities.strToMd5(usr.getPassword()));
        
        getEntityManager().persist(usr);
        addDefaultPermissions(usr);
        
        exist  = getEntityManager().find(User.class, usr.getUser());
        
        if(exist==null){
            throw new Exception(get("generic_error"));
        }
        
        
    }
    
    private void addDefaultPermissions(User usr){
        UserPermission up;
        Date date = new Date();
        
        up = new UserPermission();
        up.setUser(usr);
        up.setPostDate(date);
        
        
        
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.ADD_FAQ);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.ADD_FAQ_COMMENT);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.ADD_FAQ_SCORE);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.ADD_FAQ_TOPIC);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.DELETE_FAQ);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.DELETE_FAQ_COMMENT);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.GET_FAQ);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.GET_FAQ_SCORE);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.GET_PERMISSION_LIST);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.GET_ROOT_FAQ_TOPICS);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.GET_USER);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.LIST_FAQ);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.MODIFY_FAQ);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.MODIFY_FAQ_COMMENT);
        addPermission(up);
        up.setId(null);
        up.setServiceOption(CatalogServiceOption.MODIFY_USER);
        addPermission(up);
        
        
    }
    
    
    @ServiceOptionAnnotation(transaction = true,permissionRequired = true)
    public void deleteUser(User usr) throws Exception{
        getEntityManager().remove(usr);
    }
    
    @ServiceOptionAnnotation(transaction = true)
    public void modifyUser(User usr) throws Exception{
        
        if(usr.getUser() == null || usr.getUser().trim().equals("")){
            throw new Exception(get("setusername"));
        }
        
        if(usr.getName() == null || usr.getName().trim().equals("")){
            throw new Exception(get("setname"));
        }
        
        if(usr.getMail() == null || usr.getMail().trim().equals("")){
            throw new Exception(get("setemail"));
        }
        
        if(usr.getPassword() == null || usr.getPassword().trim().equals("")){
            throw new Exception(get("setpassword"));
        }
        
        getEntityManager().merge(usr);
    }
    
    @ServiceOptionAnnotation(transaction = true,permissionRequired = true)
    public void addPermission(UserPermission spo){
        getEntityManager().persist(spo);
    }
    
    @ServiceOptionAnnotation(transaction = true,permissionRequired = true)
    public void deletePermission(UserPermission spo){
        getEntityManager().remove(spo);
    }

    @Override
    public String getServiceName() {
        return get("adminuserservice");
    }
    
    
    
    
}
