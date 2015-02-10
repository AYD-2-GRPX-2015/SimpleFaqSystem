
package sfs.pages.beans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import sfs.Utilities;
import sfs.catalog.CatalogServiceOption;
import sfs.pages.beans.CommonBean;
import sfs.persistence.objects.Faq;
import sfs.persistence.objects.FaqTopic;
import sfs.service.common.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author WL72166
 */
/**
 *
 * @author WL72166
 */
@ManagedBean(name = "faqListBean")
@RequestScoped
public class FaqListBean extends CommonBean{

    private Faq selectedValue;
    private List<Faq> data;
    private String headText;
    private String topic;
    private String textkey;
  
    public FaqListBean() {  
        super();
        Integer topicid = null;
        
        if(topic ==null && textkey ==null){
            topic =  (String)getRequestValue("topic");
            textkey = (String) getRequestValue("textkey");
        }
        
        try {
            if (topic != null && !topic.trim().isEmpty()) {
                topicid = new Integer(topic);
                data = (List<Faq>)Service.exec(getUser(), CatalogServiceOption.GET_TOPIC_FAQS_RECURSIVE, topicid);
                data = Utilities.getActivFaqs(data);
                FaqTopic topicObj = (FaqTopic)Service.exec(getUser(), CatalogServiceOption.GET_TOPIC, topicid);
                headText = topicObj.getName();
            } else if (textkey != null) {
                data = (List<Faq>)Service.exec(getUser(), CatalogServiceOption.FIND_FAQ, textkey);
                headText = textkey;
            }

        } catch (Exception exe) {
            addMessage("generic_error", exe.getMessage());
            Utilities.printToLog("Error al colocar la lista de faqs", exe, Utilities.LOG_LEVEL_ERROR);
        }
        
    }

    public Faq getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Faq selectedValue) {
        if(selectedValue!=null){
            addCallbackParam("faq", selectedValue.getId());
        }
        this.selectedValue = selectedValue;
    }
    
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTextkey() {
        return textkey;
    }

    public void setTextkey(String textkey) {
        this.textkey = textkey;
    }
    
    public void onFaqSelect(){
        if(selectedValue!=null){
            addCallbackParam("faq", selectedValue.getId());
        }
    }
 
    public List<Faq> getData() {
        
        return data;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }
}
