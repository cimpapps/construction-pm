
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByEmail", query = "SELECT e FROM Employee e WHERE e.email = :email"),
    @NamedQuery(name = "Employee.findByFirstName", query = "SELECT e FROM Employee e WHERE e.firstName = :firstName"),
    @NamedQuery(name = "Employee.findByLastName", query = "SELECT e FROM Employee e WHERE e.lastName = :lastName")})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @JoinTable(name = "projects_have_employees", joinColumns = {
        @JoinColumn(name = "employees_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "projects_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Project> projectCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeesId")
    private Collection<Drawing> drawingCollection;
    @JoinColumn(name = "employee_positions_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EmployeePosition employeePositionsId;
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User usersId;

    public Employee() {
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Employee(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public Employee(Integer id, String email, String firstName, String lastName, Collection<Project> projectCollection, Collection<Drawing> drawingCollection, EmployeePosition employeePositionsId, User usersId) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.projectCollection = projectCollection;
        this.drawingCollection = drawingCollection;
        this.employeePositionsId = employeePositionsId;
        this.usersId = usersId;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }

    public Collection<Drawing> getDrawingCollection() {
        return drawingCollection;
    }

    public void setDrawingCollection(Collection<Drawing> drawingCollection) {
        this.drawingCollection = drawingCollection;
    }

    public EmployeePosition getEmployeePositionsId() {
        return employeePositionsId;
    }

    public void setEmployeePositionsId(EmployeePosition employeePositionsId) {
        this.employeePositionsId = employeePositionsId;
    }

    public User getUsersId() {
        return usersId;
    }

    public void setUsersId(User usersId) {
        this.usersId = usersId;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cimpapps.construction.pm.models.Employee[ id=" + id + " ]";
    }

}
