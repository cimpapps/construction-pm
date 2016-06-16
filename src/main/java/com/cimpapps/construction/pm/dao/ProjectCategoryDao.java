
package com.cimpapps.construction.pm.dao;

import com.cimpapps.construction.pm.dao.exceptions.IllegalOrphanException;
import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cimpapps.construction.pm.models.Project;
import com.cimpapps.construction.pm.models.ProjectCategory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ProjectCategoryDao implements Serializable {

    public ProjectCategoryDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProjectCategory projectCategory) {
        if (projectCategory.getProjectCollection() == null) {
            projectCategory.setProjectCollection(new ArrayList<Project>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Project> attachedProjectCollection = new ArrayList<Project>();
            for (Project projectCollectionProjectToAttach : projectCategory.getProjectCollection()) {
                projectCollectionProjectToAttach = em.getReference(projectCollectionProjectToAttach.getClass(), projectCollectionProjectToAttach.getId());
                attachedProjectCollection.add(projectCollectionProjectToAttach);
            }
            projectCategory.setProjectCollection(attachedProjectCollection);
            em.persist(projectCategory);
            for (Project projectCollectionProject : projectCategory.getProjectCollection()) {
                ProjectCategory oldProjectCategoriesIdOfProjectCollectionProject = projectCollectionProject.getProjectCategoriesId();
                projectCollectionProject.setProjectCategoriesId(projectCategory);
                projectCollectionProject = em.merge(projectCollectionProject);
                if (oldProjectCategoriesIdOfProjectCollectionProject != null) {
                    oldProjectCategoriesIdOfProjectCollectionProject.getProjectCollection().remove(projectCollectionProject);
                    oldProjectCategoriesIdOfProjectCollectionProject = em.merge(oldProjectCategoriesIdOfProjectCollectionProject);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProjectCategory projectCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProjectCategory persistentProjectCategory = em.find(ProjectCategory.class, projectCategory.getId());
            Collection<Project> projectCollectionOld = persistentProjectCategory.getProjectCollection();
            Collection<Project> projectCollectionNew = projectCategory.getProjectCollection();
            List<String> illegalOrphanMessages = null;
            for (Project projectCollectionOldProject : projectCollectionOld) {
                if (!projectCollectionNew.contains(projectCollectionOldProject)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Project " + projectCollectionOldProject + " since its projectCategoriesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Project> attachedProjectCollectionNew = new ArrayList<Project>();
            for (Project projectCollectionNewProjectToAttach : projectCollectionNew) {
                projectCollectionNewProjectToAttach = em.getReference(projectCollectionNewProjectToAttach.getClass(), projectCollectionNewProjectToAttach.getId());
                attachedProjectCollectionNew.add(projectCollectionNewProjectToAttach);
            }
            projectCollectionNew = attachedProjectCollectionNew;
            projectCategory.setProjectCollection(projectCollectionNew);
            projectCategory = em.merge(projectCategory);
            for (Project projectCollectionNewProject : projectCollectionNew) {
                if (!projectCollectionOld.contains(projectCollectionNewProject)) {
                    ProjectCategory oldProjectCategoriesIdOfProjectCollectionNewProject = projectCollectionNewProject.getProjectCategoriesId();
                    projectCollectionNewProject.setProjectCategoriesId(projectCategory);
                    projectCollectionNewProject = em.merge(projectCollectionNewProject);
                    if (oldProjectCategoriesIdOfProjectCollectionNewProject != null && !oldProjectCategoriesIdOfProjectCollectionNewProject.equals(projectCategory)) {
                        oldProjectCategoriesIdOfProjectCollectionNewProject.getProjectCollection().remove(projectCollectionNewProject);
                        oldProjectCategoriesIdOfProjectCollectionNewProject = em.merge(oldProjectCategoriesIdOfProjectCollectionNewProject);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = projectCategory.getId();
                if (findProjectCategory(id) == null) {
                    throw new NonexistentEntityException("The projectCategory with id " + id + " no longer exists.");
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
            ProjectCategory projectCategory;
            try {
                projectCategory = em.getReference(ProjectCategory.class, id);
                projectCategory.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projectCategory with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Project> projectCollectionOrphanCheck = projectCategory.getProjectCollection();
            for (Project projectCollectionOrphanCheckProject : projectCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProjectCategory (" + projectCategory + ") cannot be destroyed since the Project " + projectCollectionOrphanCheckProject + " in its projectCollection field has a non-nullable projectCategoriesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(projectCategory);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProjectCategory> findProjectCategoryEntities() {
        return findProjectCategoryEntities(true, -1, -1);
    }

    public List<ProjectCategory> findProjectCategoryEntities(int maxResults, int firstResult) {
        return findProjectCategoryEntities(false, maxResults, firstResult);
    }

    private List<ProjectCategory> findProjectCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProjectCategory.class));
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

    public ProjectCategory findProjectCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjectCategory.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProjectCategory> rt = cq.from(ProjectCategory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
