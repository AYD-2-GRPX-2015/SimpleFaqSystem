/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import sfs.TextResourcesUtil;
import sfs.Utilities;
import sfs.persistence.objects.Faq;
import sfs.service.common.Service;
import sfs.service.common.ServiceOptionAnnotation;
import static sfs.Utilities.*;
import sfs.catalog.EntidadEstado;
import sfs.persistence.objects.Comment;
import sfs.persistence.objects.FaqContact;
import sfs.persistence.objects.FaqHistory;
import sfs.persistence.objects.FaqScore;
import sfs.persistence.objects.FaqTopic;
import sfs.persistence.objects.UploadedFile;
import sfs.persistence.objects.User;
import sfs.service.objects.FaqCalculatedRanking;
import sfs.service.objects.FaqTopVisited;

/**
 *
 * @author WL72166
 */
public class AdminFaqService extends Service{
    
    @ServiceOptionAnnotation(transaction = true)
    public void addFaq(Faq faq) throws Exception{
        
        if(faq.getTitle()==null || faq.getTitle().trim().equals("")){
            throw new Exception(get("setatitle"));
        }
        
        if(faq.getShortDescription()== null || faq.getShortDescription().trim().equals("")){
            throw new Exception(get("setshordescription"));
        }
        
        if(faq.getLongDescription() == null || faq.getLongDescription().trim().equals("")){
            throw new Exception(get("setlongdescription"));
        }
        
        if(faq.getTopic() == null ){
            throw new Exception(get("settopic"));
        }
        
        if(faq.getUser()==null){
            throw new Exception(get("setuser"));
        }
        
        if(faq.getVisits()==null){
            faq.setVisits(0);
        }
        
        
        try {
            faq.setEstado(EntidadEstado.ACTIVO);
            getEntityManager().persist(faq);
            addFaqHistory(getUser(),"Se crea la entrada: "+faq.getTitle(), faq);
            refreshTopics();
        } catch (Exception e) {
            throw e;
        }
        
    }
    
    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void deleteFaq(Faq faq) throws Exception{
        getEntityManager().remove(faq);
        refreshTopics();
    }
    
    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void modifyFaq(Faq faq) throws Exception{
        
        Faq current = getEntityManager().find(Faq.class, faq.getId());
        
        if(faq.getTitle()==null || faq.getTitle().trim().equals("")){
            throw new Exception(get("setatitle"));
        }
        
        if(faq.getShortDescription()== null || faq.getShortDescription().trim().equals("")){
            throw new Exception(get("setshordescription"));
        }
        
        if(faq.getLongDescription() == null || faq.getLongDescription().trim().equals("")){
            throw new Exception(get("setlongdescription"));
        }
        
        if(faq.getTopic() == null ){
            throw new Exception(get("settopic"));
        }
        
        if(faq.getUser()==null){
            throw new Exception(get("setuser"));
        }
        
        if(!current.getUser().equals(faq.getUser())){
            throw new Exception(get("cantchangeuser"));
        }
        
        
        
        String whatModification = "";
        if(!current.getTitle().equals(faq.getTitle()))                          whatModification += " "+get("title");
        if(!current.getShortDescription().equals(faq.getShortDescription()))    whatModification += " "+get("shortdescription");
        if(!current.getLongDescription().equals(faq.getLongDescription()))      whatModification += " "+get("longdescription");
        if(!current.getTopic().equals(faq.getTopic()))                          whatModification += " "+get("topic");
        
            
        
        getEntityManager().merge(faq);
        addFaqHistory(getUser(), "Se han modificado en el faq "+faq.getTitle()+" los datos "+whatModification, faq);
        refreshTopics();
    }
    
    @ServiceOptionAnnotation
    public List<Faq> listFaqs(FaqTopic topic){
        List<Faq> list = null;
        if(topic == null){
            Query q =  getEntityManager().createNamedQuery("Faq.findAll");
            list =  q.getResultList();
        }else{
            getEntityManager().refresh(topic);
            list =  topic.getFaqList();
        }
        
        List<Faq> filtered = new ArrayList<Faq>();
        if(list!=null){
            for(Faq f : list){
                if(f.getEstado()==EntidadEstado.ACTIVO){
                    filtered.add(f);
                }
            }
        }
        
        return filtered;
    }
    
    @ServiceOptionAnnotation(transaction = true)
    public Faq getFaq(int id){
        Faq faq = getEntityManager().find(Faq.class, id);
        if(faq!=null){
            getEntityManager().refresh(faq);
        }
        return faq;
    }
    
    @ServiceOptionAnnotation(transaction = true)
    public void addVisitToFaq(Faq faq){
        Integer visitas = 1;
        if(faq.getVisits()!=null){
           visitas += faq.getVisits();
       }
       faq.setVisits(visitas);
       getEntityManager().merge(faq);
    }
    
    
    @ServiceOptionAnnotation
    public List<FaqTopic> getRootFaqTopics(){
        String qs = TextResourcesUtil.getText("query.FaqTopic.findRoot");
        
        Query q = getEntityManager().createQuery(qs);
        List<FaqTopic> list = q.getResultList();
        refreshTopics(list);
        return list;
    }  
    
    
    @ServiceOptionAnnotation
    public Long getFaqCountRecursive(FaqTopic topic){
        HashMap<Integer,Long> info = getFaqCount();
        return getFaqCountRecursive(topic,info);
    }
    
    private HashMap<Integer,Long> getFaqCount(){
        String query = TextResourcesUtil.getText("query.native.topicfaqcount");
        Query q = getEntityManager().createNativeQuery(query);
        HashMap<Integer,Long> data = new HashMap<Integer,Long>();
        
        List<Object[]> l = q.getResultList();
        if(l== null || l.isEmpty()){
            return null;
        }
        
        for(Object[] o:l){
            data.put((Integer)o[0],(Long)o[1]);
        }
        return data;
    }
    
    private Long getFaqCountRecursive(FaqTopic topic, HashMap<Integer,Long> info){
        Long sum = 0L;
        
        if(info.containsKey(topic.getId())){
            sum = info.get(topic.getId());
        }
        
        if(topic.getFaqTopicList()!=null && !topic.getFaqTopicList().isEmpty()){
            for(FaqTopic child : topic.getFaqTopicList()){
                sum += getFaqCountRecursive(child,info);
            }
        }
        return sum;
    }
    
    private void refreshTopics(){
        List<FaqTopic> topics = getRootFaqTopics();
        refreshTopics(topics);
    }
    
    
    private void refreshTopics(List<FaqTopic> topics){
        if(topics==null){
            return;
        }
        
        for(FaqTopic t:topics){
            refreshTopicsRecursive(t);
        } 
    }
    
    
    private void refreshTopicsRecursive(FaqTopic top){
        getEntityManager().refresh(top);
        if(top.getFaqList()!=null){
            for(FaqTopic ch:top.getFaqTopicList()){
                refreshTopicsRecursive(ch);
            }
        }
    }
    
    
    
    
    @ServiceOptionAnnotation
    public FaqTopic getFaqTopic(Integer id){
        FaqTopic ft= getEntityManager().find(FaqTopic.class, id);
        getEntityManager().refresh(ft);
        return ft;
    }
    
    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void addFaqTopic(FaqTopic ft){
        getEntityManager().persist(ft);
    }
    
    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void deleteFaqTopic(FaqTopic ft){
        getEntityManager().remove(ft);
    }

    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void addFaqScore(FaqScore score){
        
        Faq faq = getEntityManager().find(Faq.class, score.getFaq().getId());
        getEntityManager().refresh(faq);
        FaqScore prev = getFaqScore(score.getUser(), faq);
        
        if(prev!=null){
            getEntityManager().remove(prev);
        }
        
        getEntityManager().persist(score);
    }
    
    @ServiceOptionAnnotation
    public FaqScore getFaqScore(User user, Faq faq){
        faq = getEntityManager().find(Faq.class, faq.getId());
        getEntityManager().refresh(faq);
        List<FaqScore> list = faq.getFaqScoreList();
        if(list!=null){
            for(FaqScore fs: list){
                if(fs.getUser().getUser().equals(user.getUser())){
                    return fs;
                }
            }
        }
        return null;
    }
    
    
    @ServiceOptionAnnotation(transaction = true)
    public void deleteFaqScore(FaqScore fs){
        getEntityManager().remove(fs);
    }
    
    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void addFaqComment(Comment comment) {
        getEntityManager().persist(comment);
    }
    
    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void deleteFaqComment(Comment comment){
        getEntityManager().remove(comment);
    }
    
    @ServiceOptionAnnotation(transaction = true, permissionRequired = true)
    public void modifyFaqComment(Comment comment){
        getEntityManager().merge(comment);
    }
    
    private void addFaqHistory(User user,String comment,Faq faq){
        FaqHistory faqh = new FaqHistory();
        faqh.setComment(comment);
        faqh.setDate(new Date());
        faqh.setFaq(faq);
        faqh.setUser(user);
        getEntityManager().persist(faqh);
    }
    
    
    @ServiceOptionAnnotation(transaction = true)
    public UploadedFile addUploadedFile(UploadedFile uf) throws IOException{
        getEntityManager().persist(uf);        
        return uf;
    }
    
    
    @ServiceOptionAnnotation
    public List<Faq> findFaq(String textSearch){
        String strquery = TextResourcesUtil.getText("query.Faq.find");
        Query q = getEntityManager().createQuery(strquery);
        q.setParameter("title", "%"+textSearch+"%");
        q.setParameter("short_description", "%"+textSearch+"%");
        q.setParameter("long_description", "%"+textSearch+"%");
        List<Faq> res = q.getResultList();
        return res;
    }
    
    @ServiceOptionAnnotation
    public List<FaqCalculatedRanking> getFaqTopRanked(){
        Query q = getEntityManager().createNativeQuery(TextResourcesUtil.getText("query.native.topranked"));
        List<Object[]> res = q.getResultList();
        if(res!=null){
            List<FaqCalculatedRanking> list = new ArrayList<FaqCalculatedRanking>();
            for(Object[] o:res){
                Faq f = getFaq((Integer)o[0]);
                if(f.getEstado()!=EntidadEstado.ACTIVO) continue;
                FaqCalculatedRanking fcr = new FaqCalculatedRanking();
                fcr.setFaqObject(f);
                fcr.setRanking(Utilities.getAprox(((BigDecimal)o[1]).doubleValue()));
                fcr.setSumOfRankings(((BigDecimal)o[2]).intValue());
                fcr.setNumOfRankings(((Long)o[3]).intValue());
                list.add(fcr);
            }
            return list;
        }
        return null;
    }
    
    @ServiceOptionAnnotation
    public List<Faq> getFaqTopVisited() {

        Query q = getEntityManager().createQuery(TextResourcesUtil.getText("query.Faq.topvisited"));
        q.setMaxResults(20);
        return q.getResultList();

    }
    
    @ServiceOptionAnnotation
    public List<Faq> getLastNewFaqs(){
        Query q = getEntityManager().createQuery(TextResourcesUtil.getText("query.newfaqs"));
        q.setMaxResults(20);
        return q.getResultList();
    }
    
    @ServiceOptionAnnotation(transaction = true)
    public void addFaqContact(FaqContact contacto){
        getEntityManager().persist(contacto);
    }
    
    @ServiceOptionAnnotation(transaction = true)
    public void deleteFaqContact(FaqContact contacto){
        contacto = getEntityManager().find(FaqContact.class, contacto.getId());
        if(contacto!=null) getEntityManager().remove(contacto);
    }
    
    @ServiceOptionAnnotation
    public List<Faq> getTopicFaqsRecursive(Integer topicId){
        FaqTopic root = getFaqTopic(topicId);
        List<Faq> faqList = new ArrayList<Faq>();
        getFaqOfTopicRecurseve(root, faqList);
        return faqList;
    }
    
    private void getFaqOfTopicRecurseve(FaqTopic topic, List<Faq> faqList){
        
        if(topic.getFaqList()!=null){
            faqList.addAll(topic.getFaqList());
        }
        
        List<FaqTopic> faqTopicList = topic.getFaqTopicList();
        
        if(faqTopicList!=null){
            for(FaqTopic child: faqTopicList){
                getFaqOfTopicRecurseve(child, faqList);
            }
        }
    }
    
    @Override
    public String getServiceName() {
        return get("faqadminservice");
    }
    
    
    
    
    
}
