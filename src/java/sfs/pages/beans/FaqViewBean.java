/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;

import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.RateEvent;
import sfs.Utilities;
import sfs.catalog.CatalogServiceOption;
import sfs.persistence.objects.Faq;
import sfs.persistence.objects.FaqScore;
import sfs.persistence.objects.User;
import sfs.service.common.Service;


/**
 *
 * @author WL72166
 */
@ManagedBean(name = "faqViewBean")
@RequestScoped
public class FaqViewBean extends CommonBean{

    private Integer currentFaqId;
    private Faq currentFaq;
    Integer myraiting;
    Integer globalRating = 0;
    Integer raitingCount = 0;
    FaqScore userScore;
    
    /**
     * Creates a new instance of FaqViewBean
     */
    public FaqViewBean() {
        super();
        String faqid = (String)getRequestValue("faq");
        
        if(faqid==null){
            faqid = (String)getRequestValue("viewfaqform:faq");
        }
        
        if(faqid!=null){
            initData(new Integer(faqid));
        }else if(currentFaqId!=null){
            initData(currentFaqId);
        }
    }
    
    
    private void initData(Integer faqid){
        currentFaqId = faqid;
        User user = getUser();
        try {
            currentFaq = (Faq)Service.exec(user, CatalogServiceOption.GET_FAQ, faqid);
            if(user!=null && currentFaq!=null){
                userScore = (FaqScore)Service.exec(user, CatalogServiceOption.GET_FAQ_SCORE, user, currentFaq);
                if(userScore!=null){
                    myraiting = userScore.getLevel().intValue();
                }
            }
            
            //Calculando raiting
            Integer sum = 0;
            if (currentFaq == null || currentFaq.getFaqScoreList() == null || currentFaq.getFaqScoreList().isEmpty()) {
                return;
            }
            for (FaqScore fs : currentFaq.getFaqScoreList()) {
                sum += fs.getLevel().intValue();
            }
            raitingCount = currentFaq.getFaqScoreList().size();
            globalRating = Utilities.getAprox(sum.doubleValue() / raitingCount.doubleValue());
            
            
            //Agrega el conteo de visitas al FAQ
            Service.exec(getUser(), CatalogServiceOption.ADD_VISIT_TO_FAQ, currentFaq);
            
        } catch (Exception e) {
            Utilities.printToLog("Error al tratar de obtener el FAQ", e, Utilities.LOG_LEVEL_ERROR);
        }
    }
    

    public Faq getCurrentFaq() {
        return currentFaq;
    }

    public void setCurrentFaq(Faq currentFaq) {
        this.currentFaq = currentFaq;
    }

    public Integer getMyraiting() {
        return myraiting;
    }

    public void setMyraiting(Integer myraiting) {
        this.myraiting = myraiting;
    }

    

    public Integer getRaitingCount() {
        return raitingCount;
    }

    public void setRaitingCount(Integer raitingCount) {
        this.raitingCount = raitingCount;
    }
    
    public void onrate(RateEvent evt){
        
        Integer r = (Integer)evt.getRating();
        
        if(userScore!=null){
            addMessage("generic_error", "alreadyhasraiting");
            return;
        }
        
        if(getUser()==null){
            addMessage("generic_error", "youneedlogin");
            return;
        }
        
        if(currentFaq==null){
            addMessage("generic_error", "generic_error");
            return;
        }
        
        try {
            FaqScore score = new FaqScore();
            score.setUser(getUser());
            score.setAddedDate(new Date());
            score.setFaq(currentFaq);
            score.setLevel(r.shortValue());
            Service.exec(getUser(),CatalogServiceOption.ADD_FAQ_SCORE, score);
            //initData(currentFaq.getId());
        } catch (Exception e) {
            addMessage("generic_error", e.getMessage());
        }    
    }
    
    
    public void removeRaiting(){
        if(userScore!=null){
            try {
                Service.exec(getUser(), CatalogServiceOption.DELETE_FAQ_SCORE, userScore);
                userScore = null;
            } catch (Exception e) {
                addMessage("generic_error", e.getMessage());
            }
        }
    }
    
    public String getRaitingDisabled(){
        if(userScore!=null || getUser()==null){
            return "true";
        }
        return "false";
    }
    
    public String getRemoveScoreVisibility(){
        if(userScore==null || getUser()==null){
            return "hidden";
        }
        return "visible";
    }
    
    public String getMyRaitingVisibility(){
        if(getUser()==null){
            return "hidden";
        }
        return "visible";
    }
    
    
    public String getPostDate(){
        if(this.currentFaq==null || this.currentFaq.getPostdate()==null){
            return "";
        }
       
        return this.currentFaq.getPostdate().getDate()+"/"+(currentFaq.getPostdate().getMonth()+1)+"/"+(currentFaq.getPostdate().getYear()+1900);
    }

    public Integer getCurrentFaqId() {
        return currentFaqId;
    }

    public void setCurrentFaqId(Integer currentFaqId) {
        this.currentFaqId = currentFaqId;
    }

    public Integer getGlobalRating() {
        return globalRating;
    }

    public void setGlobalRating(Integer globalRating) {
        this.globalRating = globalRating;
    }
    
    
    
    
    
    
    
    
    
    
}
