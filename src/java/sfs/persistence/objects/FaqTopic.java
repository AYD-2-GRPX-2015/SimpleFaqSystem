/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.persistence.objects;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author WL72166
 */
@Entity
@Table(name = "sf_faq_topic")
@NamedQueries({
    @NamedQuery(name = "FaqTopic.findAll", query = "SELECT f FROM FaqTopic f"),
    @NamedQuery(name = "FaqTopic.findById", query = "SELECT f FROM FaqTopic f WHERE f.id = :id"),
    @NamedQuery(name = "FaqTopic.findByName", query = "SELECT f FROM FaqTopic f WHERE f.name = :name")})
public class FaqTopic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<FaqTopic> faqTopicList;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FaqTopic parent;
    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    private List<Faq> faqList;

    public FaqTopic() {
    }

    public FaqTopic(Integer id) {
        this.id = id;
    }

    public FaqTopic(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FaqTopic> getFaqTopicList() {
        return faqTopicList;
    }

    public void setFaqTopicList(List<FaqTopic> faqTopicList) {
        this.faqTopicList = faqTopicList;
    }

    public FaqTopic getParent() {
        return parent;
    }

    public void setParent(FaqTopic parent) {
        this.parent = parent;
    }

    public List<Faq> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<Faq> faqList) {
        this.faqList = faqList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaqTopic)) {
            return false;
        }
        FaqTopic other = (FaqTopic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sfs.persistence.objects.FaqTopic[ id=" + id + " ]";
    }
    
}
