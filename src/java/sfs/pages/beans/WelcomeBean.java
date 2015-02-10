    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import sfs.catalog.CatalogServiceOption;
import sfs.persistence.objects.Faq;
import sfs.service.common.Service;
import sfs.service.objects.FaqCalculatedRanking;
import sfs.service.objects.FaqTopVisited;


/**
 *
 * @author WL72166
 */
@ManagedBean(name = "welcomeBean")
@RequestScoped
public class WelcomeBean extends CommonBean{

    private List<FaqCalculatedRanking> topRanked;
    private List<Faq> topVisited;
    private List<Faq> lastNewFaqs;
    
    
    /** Creates a new instance of WelcomeBean */
    public WelcomeBean() {
       
        try {
            topRanked = (List<FaqCalculatedRanking>)Service.exec(getUser(), CatalogServiceOption.GET_FAQ_TOP_RANKED);
            topVisited = (List<Faq>)Service.exec(getUser(), CatalogServiceOption.GET_FAQ_TOP_VISITED);
            lastNewFaqs = (List<Faq>)Service.exec(getUser(), CatalogServiceOption.GET_LAST_NEW_FAQS);
        } catch (Exception e) {
        }
       
        
    }

    public List<FaqCalculatedRanking> getTopRanked() {
        return topRanked;
    }

    public void setTopRanked(List<FaqCalculatedRanking> topRanked) {
        this.topRanked = topRanked;
    }

    public List<Faq> getTopVisited() {
        return topVisited;
    }

    public void setTopVisited(List<Faq> topVisited) {
        this.topVisited = topVisited;
    }

    public List<Faq> getLastNewFaqs() {
        return lastNewFaqs;
    }

    public void setLastNewFaqs(List<Faq> lastNewFaqs) {
        this.lastNewFaqs = lastNewFaqs;
    }

    
    

}
