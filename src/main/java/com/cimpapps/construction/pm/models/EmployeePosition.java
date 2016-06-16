
package com.cimpapps.construction.pm.models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "employee_positions")
@NamedQueries({
    @NamedQuery(name = "EmployeePosition.findAll", query = "SELECT e FROM EmployeePosition e"),
    @NamedQuery(name = "EmployeePosition.findById", query = "SELECT e FROM EmployeePosition e WHERE e.id = :id"),
    @NamedQuery(name = "EmployeePosition.findByPosition", query = "SELECT e FROM EmployeePosition e WHERE e.position = :position")})
public class EmployeePosition implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "position")
    private String position;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeePositionsId")
    private Collection<Employee> employeeCollection;

    public EmployeePosition() {
    }

    public EmployeePosition(Integer id) {
        this.id = id;
    }

    public EmployeePosition(Integer id, String position) {
        this.id = id;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
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
        if (!(object instanceof EmployeePosition)) {
            return false;
        }
        EmployeePosition other = (EmployeePosition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cimpapps.construction.pm.models.EmployeePosition[ id=" + id + " ]";
    }

}
