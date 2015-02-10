/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.pages.beans.objects;

import sfs.Utilities;
import sfs.catalog.CatalogServiceOption;
import sfs.persistence.objects.FaqTopic;
import sfs.service.common.Service;

/**
 *
 * @author WL72166
 */
public class TreeTopicObject {

    FaqTopic topic;
    String str;

    public TreeTopicObject(FaqTopic topic) {
        this.topic = topic;
    }
    
    
    public void setToString(String str){
        this.str = str;
    }
    

    @Override
    public String toString() {
        return str;
    }

    public FaqTopic getTopic() {
        return topic;
    }

    public void setTopic(FaqTopic topic) {
        this.topic = topic;
    }
    
}
