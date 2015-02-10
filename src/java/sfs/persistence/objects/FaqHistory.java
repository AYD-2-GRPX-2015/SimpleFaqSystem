/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.persistence.objects;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author WL72166
 */
@Entity
@Table(name = "sf_faq_history")
@NamedQueries({
    @NamedQuery(name = "FaqHistory.findAll", query = "SELECT f FROM FaqHistory f"),
    @NamedQuery(name = "FaqHistory.findById", query = "SELECT f FROM FaqHistory f WHERE f.id = :id"),
    @NamedQuery(name = "FaqHistory.findByDate", query = "SELECT f FROM FaqHistory f WHERE f.date = :date"),
    @NamedQuery(name = "FaqHistory.findByComment", query = "SELECT f FROM FaqHistory f WHERE f.comment = :comment")})
public class FaqHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "user", referencedColumnName = "user")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @JoinColumn(name = "faq", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faq faq;

    public FaqHistory() {
    }

    public FaqHistory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
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
        if (!(object instanceof FaqHistory)) {
            return false;
        }
        FaqHistory other = (FaqHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sfs.persistence.objects.FaqHistory[ id=" + id + " ]";
    }
    
}
