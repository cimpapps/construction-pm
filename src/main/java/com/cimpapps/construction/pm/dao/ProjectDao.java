
package com.cimpapps.construction.pm.dao;

import com.cimpapps.construction.pm.dao.exceptions.IllegalOrphanException;
import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import com.cimpapps.construction.pm.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cimpapps.construction.pm.models.ProjectCategory;
import com.cimpapps.construction.pm.models.Employee;
import com.cimpapps.construction.pm.models.Project;
import java.util.ArrayList;
import java.util.Collection;
import com.cimpapps.construction.pm.models.ProjectLayer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ProjectDao implements Serializable {

    public ProjectDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Project project) throws PreexistingEntityException, Exception {
        if (project.getEmployeeCollection() == null) {
            project.setEmployeeCollection(new ArrayList<Employee>());
        }
        if (project.getProjectLayerCollection() == null) {
            project.setProjectLayerCollection(new ArrayList<ProjectLayer>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProjectCategory projectCategoriesId = project.getProjectCategoriesId();
            if (projectCategoriesId != null) {
                projectCategoriesId = em.getReference(projectCategoriesId.getClass(), projectCategoriesId.getId());
                project.setProjectCategoriesId(projectCategoriesId);
            }
            Collection<Employee> attachedEmployeeCollection = new ArrayList<Employee>();
            for (Employee employeeCollectionEmployeeToAttach : project.getEmployeeCollection()) {
                employeeCollectionEmployeeToAttach = em.getReference(employeeCollectionEmployeeToAttach.getClass(), employeeCollectionEmployeeToAttach.getId());
                attachedEmployeeCollection.add(employeeCollectionEmployeeToAttach);
            }
            project.setEmployeeCollection(attachedEmployeeCollection);
            Collection<ProjectLayer> attachedProjectLayerCollection = new ArrayList<ProjectLayer>();
            for (ProjectLayer projectLayerCollectionProjectLayerToAttach : project.getProjectLayerCollection()) {
                projectLayerCollectionProjectLayerToAttach = em.getReference(projectLayerCollectionProjectLayerToAttach.getClass(), projectLayerCollectionProjectLayerToAttach.getId());
                attachedProjectLayerCollection.add(projectLayerCollectionProjectLayerToAttach);
            }
            project.setProjectLayerCollection(attachedProjectLayerCollection);
            em.persist(project);
            if (projectCategoriesId != null) {
                projectCategoriesId.getProjectCollection().add(project);
                projectCategoriesId = em.merge(projectCategoriesId);
            }
            for (Employee employeeCollectionEmployee : project.getEmployeeCollection()) {
                employeeCollectionEmployee.getProjectCollection().add(project);
                employeeCollectionEmployee = em.merge(employeeCollectionEmployee);
            }
            for (ProjectLayer projectLayerCollectionProjectLayer : project.getProjectLayerCollection()) {
                Project oldProjectsIdOfProjectLayerCollectionProjectLayer = projectLayerCollectionProjectLayer.getProjectsId();
                projectLayerCollectionProjectLayer.setProjectsId(project);
                projectLayerCollectionProjectLayer = em.merge(projectLayerCollectionProjectLayer);
                if (oldProjectsIdOfProjectLayerCollectionProjectLayer != null) {
                    oldProjectsIdOfProjectLayerCollectionProjectLayer.getProjectLayerCollection().remove(projectLayerCollectionProjectLayer);
                    oldProjectsIdOfProjectLayerCollectionProjectLayer = em.merge(oldProjectsIdOfProjectLayerCollectionProjectLayer);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Project project) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Project persistentProject = em.find(Project.class, project.getId());
            ProjectCategory projectCategoriesIdOld = persistentProject.getProjectCategoriesId();
            ProjectCategory projectCategoriesIdNew = project.getProjectCategoriesId();
            Collection<Employee> employeeCollectionOld = persistentProject.getEmployeeCollection();
            Collection<Employee> employeeCollectionNew = project.getEmployeeCollection();
            Collection<ProjectLayer> projectLayerCollectionOld = persistentProject.getProjectLayerCollection();
            Collection<ProjectLayer> projectLayerCollectionNew = project.getProjectLayerCollection();
            List<String> illegalOrphanMessages = null;
            for (ProjectLayer projectLayerCollectionOldProjectLayer : projectLayerCollectionOld) {
                if (!projectLayerCollectionNew.contains(projectLayerCollectionOldProjectLayer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProjectLayer " + projectLayerCollectionOldProjectLayer + " since its projectsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (projectCategoriesIdNew != null) {
                projectCategoriesIdNew = em.getReference(projectCategoriesIdNew.getClass(), projectCategoriesIdNew.getId());
                project.setProjectCategoriesId(projectCategoriesIdNew);
            }
            Collection<Employee> attachedEmployeeCollectionNew = new ArrayList<Employee>();
            for (Employee employeeCollectionNewEmployeeToAttach : employeeCollectionNew) {
                employeeCollectionNewEmployeeToAttach = em.getReference(employeeCollectionNewEmployeeToAttach.getClass(), employeeCollectionNewEmployeeToAttach.getId());
                attachedEmployeeCollectionNew.add(employeeCollectionNewEmployeeToAttach);
            }
            employeeCollectionNew = attachedEmployeeCollectionNew;
            project.setEmployeeCollection(employeeCollectionNew);
            Collection<ProjectLayer> attachedProjectLayerCollectionNew = new ArrayList<ProjectLayer>();
            for (ProjectLayer projectLayerCollectionNewProjectLayerToAttach : projectLayerCollectionNew) {
                projectLayerCollectionNewProjectLayerToAttach = em.getReference(projectLayerCollectionNewProjectLayerToAttach.getClass(), projectLayerCollectionNewProjectLayerToAttach.getId());
                attachedProjectLayerCollectionNew.add(projectLayerCollectionNewProjectLayerToAttach);
            }
            projectLayerCollectionNew = attachedProjectLayerCollectionNew;
            project.setProjectLayerCollection(projectLayerCollectionNew);
            project = em.merge(project);
            if (projectCategoriesIdOld != null && !projectCategoriesIdOld.equals(projectCategoriesIdNew)) {
                projectCategoriesIdOld.getProjectCollection().remove(project);
                projectCategoriesIdOld = em.merge(projectCategoriesIdOld);
            }
            if (projectCategoriesIdNew != null && !projectCategoriesIdNew.equals(projectCategoriesIdOld)) {
                projectCategoriesIdNew.getProjectCollection().add(project);
                projectCategoriesIdNew = em.merge(projectCategoriesIdNew);
            }
            for (Employee employeeCollectionOldEmployee : employeeCollectionOld) {
                if (!employeeCollectionNew.contains(employeeCollectionOldEmployee)) {
                    employeeCollectionOldEmployee.getProjectCollection().remove(project);
                    employeeCollectionOldEmployee = em.merge(employeeCollectionOldEmployee);
                }
            }
            for (Employee employeeCollectionNewEmployee : employeeCollectionNew) {
                if (!employeeCollectionOld.contains(employeeCollectionNewEmployee)) {
                    employeeCollectionNewEmployee.getProjectCollection().add(project);
                    employeeCollectionNewEmployee = em.merge(employeeCollectionNewEmployee);
                }
            }
            for (ProjectLayer projectLayerCollectionNewProjectLayer : projectLayerCollectionNew) {
                if (!projectLayerCollectionOld.contains(projectLayerCollectionNewProjectLayer)) {
                    Project oldProjectsIdOfProjectLayerCollectionNewProjectLayer = projectLayerCollectionNewProjectLayer.getProjectsId();
                    projectLayerCollectionNewProjectLayer.setProjectsId(project);
                    projectLayerCollectionNewProjectLayer = em.merge(projectLayerCollectionNewProjectLayer);
                    if (oldProjectsIdOfProjectLayerCollectionNewProjectLayer != null && !oldProjectsIdOfProjectLayerCollectionNewProjectLayer.equals(project)) {
                        oldProjectsIdOfProjectLayerCollectionNewProjectLayer.getProjectLayerCollection().remove(projectLayerCollectionNewProjectLayer);
                        oldProjectsIdOfProjectLayerCollectionNewProjectLayer = em.merge(oldProjectsIdOfProjectLayerCollectionNewProjectLayer);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = project.getId();
                if (findProject(id) == null) {
                    throw new NonexistentEntityException("The project with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Project project;
            try {
                project = em.getReference(Project.class, id);
                project.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The project with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProjectLayer> projectLayerCollectionOrphanCheck = project.getProjectLayerCollection();
            for (ProjectLayer projectLayerCollectionOrphanCheckProjectLayer : projectLayerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Project (" + project + ") cannot be destroyed since the ProjectLayer " + projectLayerCollectionOrphanCheckProjectLayer + " in its projectLayerCollection field has a non-nullable projectsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ProjectCategory projectCategoriesId = project.getProjectCategoriesId();
            if (projectCategoriesId != null) {
                projectCategoriesId.getProjectCollection().remove(project);
                projectCategoriesId = em.merge(projectCategoriesId);
            }
            Collection<Employee> employeeCollection = project.getEmployeeCollection();
            for (Employee employeeCollectionEmployee : employeeCollection) {
                employeeCollectionEmployee.getProjectCollection().remove(project);
                employeeCollectionEmployee = em.merge(employeeCollectionEmployee);
            }
            em.remove(project);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Project> findProjectEntities() {
        return findProjectEntities(true, -1, -1);
    }

    public List<Project> findProjectEntities(int maxResults, int firstResult) {
        return findProjectEntities(false, maxResults, firstResult);
    }

    private List<Project> findProjectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Project.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Project findProject(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Project.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Project> rt = cq.from(Project.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
