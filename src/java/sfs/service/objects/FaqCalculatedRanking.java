/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.service.objects;

import sfs.persistence.objects.Faq;

/**
 *
 * @author WL72166
 */
public class FaqCalculatedRanking{
    
    private Faq faqObject;
    private Integer ranking;
    private Integer sumOfRankings;
    private Integer numOfRankings;

    public Faq getFaqObject() {
        return faqObject;
    }

    public void setFaqObject(Faq faqObject) {
        this.faqObject = faqObject;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getSumOfRankings() {
        return sumOfRankings;
    }

    public void setSumOfRankings(Integer sumOfRankings) {
        this.sumOfRankings = sumOfRankings;
    }

    public Integer getNumOfRankings() {
        return numOfRankings;
    }

    public void setNumOfRankings(Integer numOfRankings) {
        this.numOfRankings = numOfRankings;
    }


    
    
    
}
