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
@Table(name = "sf_uploaded_file")
@NamedQueries({
    @NamedQuery(name = "UploadedFile.findAll", query = "SELECT u FROM UploadedFile u"),
    @NamedQuery(name = "UploadedFile.findById", query = "SELECT u FROM UploadedFile u WHERE u.id = :id"),
    @NamedQuery(name = "UploadedFile.findByName", query = "SELECT u FROM UploadedFile u WHERE u.name = :name"),
    @NamedQuery(name = "UploadedFile.findByFilesize", query = "SELECT u FROM UploadedFile u WHERE u.filesize = :filesize"),
    @NamedQuery(name = "UploadedFile.findByDescription", query = "SELECT u FROM UploadedFile u WHERE u.description = :description"),
    @NamedQuery(name = "UploadedFile.findByUpladDate", query = "SELECT u FROM UploadedFile u WHERE u.upladDate = :upladDate"),
    @NamedQuery(name = "UploadedFile.findByPath", query = "SELECT u FROM UploadedFile u WHERE u.path = :path")})
public class UploadedFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "filesize")
    private Integer filesize;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "uplad_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upladDate;
    @Basic(optional = false)
    @Column(name = "path")
    private String path;
    @JoinColumn(name = "user", referencedColumnName = "user")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @JoinColumn(name = "faq", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faq faq;

    public UploadedFile() {
    }

    public UploadedFile(Integer id) {
        this.id = id;
    }

    public UploadedFile(Integer id, String name, Date upladDate, String path) {
        this.id = id;
        this.name = name;
        this.upladDate = upladDate;
        this.path = path;
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

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpladDate() {
        return upladDate;
    }

    public void setUpladDate(Date upladDate) {
        this.upladDate = upladDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        if (!(object instanceof UploadedFile)) {
            return false;
        }
        UploadedFile other = (UploadedFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sfs.persistence.objects.UploadedFile[ id=" + id + " ]";
    }
    
}
