/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.persistence.objects;

import java.io.Serializable;
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

/**
 *
 * @author WL72166
 */
@Entity
@Table(name = "sf_faq_contact")
@NamedQueries({
    @NamedQuery(name = "FaqContact.findAll", query = "SELECT f FROM FaqContact f"),
    @NamedQuery(name = "FaqContact.findById", query = "SELECT f FROM FaqContact f WHERE f.id = :id"),
    @NamedQuery(name = "FaqContact.findByName", query = "SELECT f FROM FaqContact f WHERE f.name = :name"),
    @NamedQuery(name = "FaqContact.findByPhone1", query = "SELECT f FROM FaqContact f WHERE f.phone1 = :phone1"),
    @NamedQuery(name = "FaqContact.findByPhone2", query = "SELECT f FROM FaqContact f WHERE f.phone2 = :phone2"),
    @NamedQuery(name = "FaqContact.findByEmail1", query = "SELECT f FROM FaqContact f WHERE f.email1 = :email1"),
    @NamedQuery(name = "FaqContact.findByEmail2", query = "SELECT f FROM FaqContact f WHERE f.email2 = :email2"),
    @NamedQuery(name = "FaqContact.findByArea", query = "SELECT f FROM FaqContact f WHERE f.area = :area")})
public class FaqContact implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone1")
    private String phone1;
    @Column(name = "phone2")
    private String phone2;
    @Column(name = "email1")
    private String email1;
    @Column(name = "email2")
    private String email2;
    @Column(name = "area")
    private String area;
    @JoinColumn(name = "faq", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faq faq;

    public FaqContact() {
    }

    public FaqContact(Integer id) {
        this.id = id;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
        if (!(object instanceof FaqContact)) {
            return false;
        }
        FaqContact other = (FaqContact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sfs.persistence.objects.FaqContact[ id=" + id + " ]";
    }
    
}
