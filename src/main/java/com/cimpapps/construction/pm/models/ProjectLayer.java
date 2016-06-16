
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "project_layers")
@NamedQueries({
    @NamedQuery(name = "ProjectLayer.findAll", query = "SELECT p FROM ProjectLayer p"),
    @NamedQuery(name = "ProjectLayer.findById", query = "SELECT p FROM ProjectLayer p WHERE p.id = :id"),
    @NamedQuery(name = "ProjectLayer.findByName", query = "SELECT p FROM ProjectLayer p WHERE p.name = :name")})
public class ProjectLayer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectLayersId")
    private Collection<Drawing> drawingCollection;
    @JoinColumn(name = "projects_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Project projectsId;

    public ProjectLayer() {
    }

    public ProjectLayer(Integer id) {
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

    public Collection<Drawing> getDrawingCollection() {
        return drawingCollection;
    }

    public void setDrawingCollection(Collection<Drawing> drawingCollection) {
        this.drawingCollection = drawingCollection;
    }

    public Project getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(Project projectsId) {
        this.projectsId = projectsId;
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
        if (!(object instanceof ProjectLayer)) {
            return false;
        }
        ProjectLayer other = (ProjectLayer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cimpapps.construction.pm.models.ProjectLayer[ id=" + id + " ]";
    }

}
