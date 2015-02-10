/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.persistence;

import java.io.FileInputStream;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author WL72166
 */
public class PersistenceUtils {
    
    private static EntityManagerFactory emf = null;
    private static EntityManager entityManager;

    
    public static EntityManager getEntityManager(){
        //if(entityManager==null){
            try {
                if (emf == null) {
                    Properties prop = new Properties();
                    prop.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/simplefaq");
                    prop.put("javax.persistence.jdbc.password", "");
                    prop.put("javax.persistence.jdbc.user", "root");
                    prop.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
                    emf = Persistence.createEntityManagerFactory("SimpleFaqSystemPU", prop);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
 
            entityManager=emf.createEntityManager();
     //   }
        return entityManager;
    }
    
    
}
