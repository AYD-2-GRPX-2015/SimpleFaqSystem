/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.service.common;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import sfs.Utilities;
import static sfs.Utilities.*;
import sfs.persistence.PersistenceUtils;
import sfs.persistence.objects.User;
import sfs.persistence.objects.UserPermission;
import sfs.service.UserAdminService;
import sfs.service.exceptions.PermissionDeniedException;
import sfs.service.objects.ServiceOption;

/**
 *
 * @author WL72166
 */
public abstract class Service {
    
    protected EntityManager entityManager = null;
    private User user;
    
    public static Object exec(User user,String serviceOptionName,Object... args) throws PermissionDeniedException, Exception{
        
        
        printToLog("Starting service option: "+serviceOptionName, null, LOG_LEVEL_INFO);
        ServiceOption so = ServiceFactory.getServiceOptionById(serviceOptionName);
        if(so==null){
            printToLog("service option: "+serviceOptionName+" NOT FOUND", null, LOG_LEVEL_ERROR);
            throw new Exception(get("invalidoption"));
        }
        
        printToLog("Validating permissions for user: "+(user!=null?user.getUser():null)+" and service: "+serviceOptionName, null, LOG_LEVEL_INFO);
        if(!validatePermission(user, so)){
            printToLog("Permission denied", null, LOG_LEVEL_INFO);
            throw new PermissionDeniedException(get("unautorizedaccess"));
        }
        
        Service srv = ServiceFactory.getInstance(so.getServiceClass());
        srv.setUser(user);
        EntityManager em = PersistenceUtils.getEntityManager();
        srv.setEntityManager(em);
        EntityTransaction et = null;        
        
        Object resp = null;
        try {
            if(so.isTransaction()){
                printToLog("Starting transaction...", null, LOG_LEVEL_INFO);
                et = srv.getEntityManager().getTransaction();
                et.begin();
            }
            
            printToLog("Executing service option "+serviceOptionName, null, LOG_LEVEL_INFO);
            
            resp = so.getMethod().invoke(srv, args);
            
            if(so.isTransaction() && et.isActive()){
                printToLog("Commit transaction.", null, LOG_LEVEL_INFO);
                et.commit();
            }
            
            printToLog("Service option execution successfull", null, LOG_LEVEL_INFO);
            
        } catch (Exception e) {
            printToLog("Error when executing service option: "+serviceOptionName+":"+e.getMessage(), e, LOG_LEVEL_ERROR);
            if(so.isTransaction() && et.isActive()) et.rollback();
            
            if(e instanceof InvocationTargetException){
                Throwable t = ((InvocationTargetException)e).getTargetException();
                if(t instanceof Exception) throw (Exception)t;
            }
            
            throw e;
        }finally{
            em.close();
        }
        
        return resp;
    }
    
    private static boolean validatePermission(User user, ServiceOption so) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException{
        
        if(!so.isPermissionRequired()){
            return true;
        }
        
        if(user == null){
            return false;
        }
        
        if(user.getUser().equals("admin")){
            return true;
        }
            
        
        UserAdminService srv = ServiceFactory.getInstance(UserAdminService.class);
        List<UserPermission> upList = srv.getPermissionList(user);
        
        for(UserPermission up: upList){
            if(up.getServiceOption().equals(so.getServiceOption())){
                    return true;
            }
        }
        
        return false;
    }
    
    protected EntityManager getEntityManager(){
        return entityManager;
    }
    
    private void setEntityManager(EntityManager em){
        entityManager = em;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    
    
    
    
    
    public abstract String getServiceName();    
}
