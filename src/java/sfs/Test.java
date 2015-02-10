/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs;

import java.util.Date;
import java.util.List;
import sfs.catalog.CatalogServiceOption;
import sfs.persistence.objects.Faq;
import sfs.persistence.objects.FaqTopic;
import sfs.persistence.objects.User;
import sfs.service.common.Service;

/**
 *
 * @author WL72166
 */
public class Test {

    
    public static void main(String[] args) throws Exception {
        try {
            
//            TextResourcesUtil.loadResource("C:\\usr\\wflc\\NetBeansProjects\\SimpleFaqSystem\\web\\resources\\text.properties");
//            
//            User u = (User)Service.exec(null, CatalogServiceOption.GET_USER, "admin", "admin");
//            if(u==null){
//                System.out.println("Bad username or password");
//                return;
//            }
//            Faq f = new Faq();
//            List<FaqTopic> rootTopics = (List<FaqTopic>)Service.exec(u, CatalogServiceOption.GET_ROOT_FAQ_TOPICS);
//            if(rootTopics == null || rootTopics.isEmpty()){
//                System.out.println("no topic found");
//                return;
//            }
//            f.setTopic(rootTopics.get(0));
//            f.setLongDescription(null);
//            f.setPostdate(new Date());
//            f.setShortDescription("descripcion corta");
//            f.setTitle("titulo del faq");
//            f.setUser(u);
//            
//            Object obj = Service.exec(u, CatalogServiceOption.ADD_FAQ,f);
//            System.out.println("obj:" + obj);
//            Object obj = Service.exec(null, CatalogServiceOption.GET_FAQ_TOP_VISITED);
//            System.out.println("obj:"+obj);
            
            FaqTopic faqtopic = (FaqTopic)Service.exec(null, CatalogServiceOption.GET_TOPIC, 2);
            Long i = (Long)Service.exec(null, CatalogServiceOption.GET_FAQ_COUNT_RECURSIVE, faqtopic);
            
            
            System.out.println(i);
            
        } catch (Exception e) {
            System.out.println("mensaje:" +e.getMessage());
        }
    }

}
