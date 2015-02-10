/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.persistence.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author WL72166
 */
@Entity
@Table(name = "sf_faq")
@NamedQueries({
    @NamedQuery(name = "Faq.findAll", query = "SELECT f FROM Faq f"),
    @NamedQuery(name = "Faq.findById", query = "SELECT f FROM Faq f WHERE f.id = :id"),
    @NamedQuery(name = "Faq.findByPostdate", query = "SELECT f FROM Faq f WHERE f.postdate = :postdate"),
    @NamedQuery(name = "Faq.findByTitle", query = "SELECT f FROM Faq f WHERE f.title = :title"),
    @NamedQuery(name = "Faq.findByShortDescription", query = "SELECT f FROM Faq f WHERE f.shortDescription = :shortDescription"),
    @NamedQuery(name = "Faq.findByVisits", query = "SELECT f FROM Faq f WHERE f.visits = :visits"),
    @NamedQuery(name = "Faq.findByEstado", query = "SELECT f FROM Faq f WHERE f.estado = :estado")})
public class Faq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "postdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postdate;
    @Column(name = "title")
    private String title;
    @Column(name = "short_description")
    private String shortDescription;
    @Lob
    @Column(name = "long_description")
    private String longDescription;
    @Column(name = "visits")
    private Integer visits;
    @Column(name = "estado")
    private Integer estado;
    @OneToMany(mappedBy = "faq", fetch = FetchType.LAZY)
    private List<FaqHistory> faqHistoryList;
    @OneToMany(mappedBy = "faq", fetch = FetchType.LAZY)
    private List<FaqContact> faqContactList;
    @JoinColumn(name = "topic", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FaqTopic topic;
    @JoinColumn(name = "user", referencedColumnName = "user")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "faq", fetch = FetchType.LAZY)
    private List<Comment> commentList;
    @OneToMany(mappedBy = "faq", fetch = FetchType.LAZY)
    private List<FaqScore> faqScoreList;
    @OneToMany(mappedBy = "faq", fetch = FetchType.LAZY)
    private List<UploadedFile> uploadedFileList;

    public Faq() {
    }

    public Faq(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPostdate() {
        return postdate;
    }

    public void setPostdate(Date postdate) {
        this.postdate = postdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<FaqHistory> getFaqHistoryList() {
        return faqHistoryList;
    }

    public void setFaqHistoryList(List<FaqHistory> faqHistoryList) {
        this.faqHistoryList = faqHistoryList;
    }

    public List<FaqContact> getFaqContactList() {
        return faqContactList;
    }

    public void setFaqContactList(List<FaqContact> faqContactList) {
        this.faqContactList = faqContactList;
    }

    public FaqTopic getTopic() {
        return topic;
    }

    public void setTopic(FaqTopic topic) {
        this.topic = topic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<FaqScore> getFaqScoreList() {
        return faqScoreList;
    }

    public void setFaqScoreList(List<FaqScore> faqScoreList) {
        this.faqScoreList = faqScoreList;
    }

    public List<UploadedFile> getUploadedFileList() {
        return uploadedFileList;
    }

    public void setUploadedFileList(List<UploadedFile> uploadedFileList) {
        this.uploadedFileList = uploadedFileList;
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
        if (!(object instanceof Faq)) {
            return false;
        }
        Faq other = (Faq) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sfs.persistence.objects.Faq[ id=" + id + " ]";
    }
    
}
