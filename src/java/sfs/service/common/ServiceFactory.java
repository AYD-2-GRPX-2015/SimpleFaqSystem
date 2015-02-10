/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.service.common;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import static sfs.Utilities.*;
import sfs.service.objects.ServiceOption;

/**
 *
 * @author WL72166
 */
public class ServiceFactory {

    
    private static Hashtable<Class,Service> cache = new Hashtable<Class, Service>();
    private static Vector<ServiceOption> publicOptionsCache;
    
    

    public static <T extends Service> T getInstance(Class<T> serviceClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException{
        if(!cache.containsKey(serviceClass)){
            serviceClass.getConstructor().setAccessible(true);
            cache.put(serviceClass, serviceClass.newInstance());
        }
        return (T)cache.get(serviceClass);
    }
    
    private static List<Class> listServices() throws IOException{
        
        String pack = "sfs/service";
        List<Class> classes = getClasses(ServiceFactory.class.getClassLoader(), pack);
        List<Class> services = new ArrayList<Class>();
        
        for(Class c:classes){
            if(isInstance(c, Service.class) && !c.equals(Service.class)){
                services.add(c);
            }
        }
        return services;
    }
    
    public static ServiceOption getServiceOptionById(String Id) throws IOException{
        List<ServiceOption> poc = getServiceOptions();
        for(ServiceOption sotmp:poc){
            if(sotmp.getServiceOption().equals(Id)){
                return sotmp;
            }
        }
        return null;
    }
    
    public static List<ServiceOption> getServiceOptions() throws IOException{
        if(publicOptionsCache!=null){
            return publicOptionsCache;
        }
        List<Class> services = listServices();
        publicOptionsCache = new Vector<ServiceOption>();
        
        for(Class c:services){
            Method methods[] = c.getMethods();
            for(Method m : methods){
                ServiceOptionAnnotation spo = (ServiceOptionAnnotation)m.getAnnotation(ServiceOptionAnnotation.class);
                if(spo!=null){
                    ServiceOption so = new ServiceOption();
                    so.setServiceOption(m.getName());
                    so.setMethod(m);
                    so.setName(get(c.getSimpleName()+"."+m.getName()));
                    so.setServiceClass(c);
                    so.setPermissionRequired(spo.permissionRequired());
                    so.setTransaction(spo.transaction());
                    publicOptionsCache.add(so);
                }
            }
        }
        
        return publicOptionsCache;
        
    }
}
