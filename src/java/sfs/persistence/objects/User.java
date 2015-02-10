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
import javax.persistence.Id;
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
@Table(name = "sf_user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUser", query = "SELECT u FROM User u WHERE u.user = :user"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findByMail", query = "SELECT u FROM User u WHERE u.mail = :mail"),
    @NamedQuery(name = "User.findByCreationdate", query = "SELECT u FROM User u WHERE u.creationdate = :creationdate"),
    @NamedQuery(name = "User.findByEstado", query = "SELECT u FROM User u WHERE u.estado = :estado")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user")
    private String user;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "mail")
    private String mail;
    @Column(name = "creationdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationdate;
    @Column(name = "estado")
    private Integer estado;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FaqHistory> faqHistoryList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Faq> faqList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> commentList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FaqScore> faqScoreList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserPermission> userPermissionList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UploadedFile> uploadedFileList;

    public User() {
    }

    public User(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
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

    public List<Faq> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<Faq> faqList) {
        this.faqList = faqList;
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

    public List<UserPermission> getUserPermissionList() {
        return userPermissionList;
    }

    public void setUserPermissionList(List<UserPermission> userPermissionList) {
        this.userPermissionList = userPermissionList;
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
        hash += (user != null ? user.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.user == null && other.user != null) || (this.user != null && !this.user.equals(other.user))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sfs.persistence.objects.User[ user=" + user + " ]";
    }
    
}
