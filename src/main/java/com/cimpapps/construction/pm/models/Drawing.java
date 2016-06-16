
package com.cimpapps.construction.pm.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "drawings")
@NamedQueries({
    @NamedQuery(name = "Drawing.findAll", query = "SELECT d FROM Drawing d"),
    @NamedQuery(name = "Drawing.findById", query = "SELECT d FROM Drawing d WHERE d.id = :id"),
    @NamedQuery(name = "Drawing.findByNumber", query = "SELECT d FROM Drawing d WHERE d.number = :number"),
    @NamedQuery(name = "Drawing.findByDescription", query = "SELECT d FROM Drawing d WHERE d.description = :description"),
    @NamedQuery(name = "Drawing.findByDateStarted", query = "SELECT d FROM Drawing d WHERE d.dateStarted = :dateStarted"),
    @NamedQuery(name = "Drawing.findByDateFinished", query = "SELECT d FROM Drawing d WHERE d.dateFinished = :dateFinished"),
    @NamedQuery(name = "Drawing.findByDateDue", query = "SELECT d FROM Drawing d WHERE d.dateDue = :dateDue")})
public class Drawing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "number")
    private String number;
    @Column(name = "description")
    private String description;
    @Column(name = "date_started")
    @Temporal(TemporalType.DATE)
    private Date dateStarted;
    @Column(name = "date_finished")
    @Temporal(TemporalType.DATE)
    private Date dateFinished;
    @Column(name = "date_due")
    @Temporal(TemporalType.DATE)
    private Date dateDue;
    @JoinColumn(name = "employees_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeesId;
    @JoinColumn(name = "project_layers_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProjectLayer projectLayersId;

    public Drawing() {
    }

    public Drawing(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Date getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(Date dateFinished) {
        this.dateFinished = dateFinished;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public Employee getEmployeesId() {
        return employeesId;
    }

    public void setEmployeesId(Employee employeesId) {
        this.employeesId = employeesId;
    }

    public ProjectLayer getProjectLayersId() {
        return projectLayersId;
    }

    public void setProjectLayersId(ProjectLayer projectLayersId) {
        this.projectLayersId = projectLayersId;
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
        if (!(object instanceof Drawing)) {
            return false;
        }
        Drawing other = (Drawing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cimpapps.construction.pm.models.Drawing[ id=" + id + " ]";
    }

}
