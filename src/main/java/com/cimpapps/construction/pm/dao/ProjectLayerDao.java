
package com.cimpapps.construction.pm.dao;

import com.cimpapps.construction.pm.dao.exceptions.IllegalOrphanException;
import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cimpapps.construction.pm.models.Project;
import com.cimpapps.construction.pm.models.Drawing;
import com.cimpapps.construction.pm.models.ProjectLayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ProjectLayerDao implements Serializable {

    public ProjectLayerDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProjectLayer projectLayer) {
        if (projectLayer.getDrawingCollection() == null) {
            projectLayer.setDrawingCollection(new ArrayList<Drawing>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Project projectsId = projectLayer.getProjectsId();
            if (projectsId != null) {
                projectsId = em.getReference(projectsId.getClass(), projectsId.getId());
                projectLayer.setProjectsId(projectsId);
            }
            Collection<Drawing> attachedDrawingCollection = new ArrayList<Drawing>();
            for (Drawing drawingCollectionDrawingToAttach : projectLayer.getDrawingCollection()) {
                drawingCollectionDrawingToAttach = em.getReference(drawingCollectionDrawingToAttach.getClass(), drawingCollectionDrawingToAttach.getId());
                attachedDrawingCollection.add(drawingCollectionDrawingToAttach);
            }
            projectLayer.setDrawingCollection(attachedDrawingCollection);
            em.persist(projectLayer);
            if (projectsId != null) {
                projectsId.getProjectLayerCollection().add(projectLayer);
                projectsId = em.merge(projectsId);
            }
            for (Drawing drawingCollectionDrawing : projectLayer.getDrawingCollection()) {
                ProjectLayer oldProjectLayersIdOfDrawingCollectionDrawing = drawingCollectionDrawing.getProjectLayersId();
                drawingCollectionDrawing.setProjectLayersId(projectLayer);
                drawingCollectionDrawing = em.merge(drawingCollectionDrawing);
                if (oldProjectLayersIdOfDrawingCollectionDrawing != null) {
                    oldProjectLayersIdOfDrawingCollectionDrawing.getDrawingCollection().remove(drawingCollectionDrawing);
                    oldProjectLayersIdOfDrawingCollectionDrawing = em.merge(oldProjectLayersIdOfDrawingCollectionDrawing);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProjectLayer projectLayer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProjectLayer persistentProjectLayer = em.find(ProjectLayer.class, projectLayer.getId());
            Project projectsIdOld = persistentProjectLayer.getProjectsId();
            Project projectsIdNew = projectLayer.getProjectsId();
            Collection<Drawing> drawingCollectionOld = persistentProjectLayer.getDrawingCollection();
            Collection<Drawing> drawingCollectionNew = projectLayer.getDrawingCollection();
            List<String> illegalOrphanMessages = null;
            for (Drawing drawingCollectionOldDrawing : drawingCollectionOld) {
                if (!drawingCollectionNew.contains(drawingCollectionOldDrawing)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Drawing " + drawingCollectionOldDrawing + " since its projectLayersId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (projectsIdNew != null) {
                projectsIdNew = em.getReference(projectsIdNew.getClass(), projectsIdNew.getId());
                projectLayer.setProjectsId(projectsIdNew);
            }
            Collection<Drawing> attachedDrawingCollectionNew = new ArrayList<Drawing>();
            for (Drawing drawingCollectionNewDrawingToAttach : drawingCollectionNew) {
                drawingCollectionNewDrawingToAttach = em.getReference(drawingCollectionNewDrawingToAttach.getClass(), drawingCollectionNewDrawingToAttach.getId());
                attachedDrawingCollectionNew.add(drawingCollectionNewDrawingToAttach);
            }
            drawingCollectionNew = attachedDrawingCollectionNew;
            projectLayer.setDrawingCollection(drawingCollectionNew);
            projectLayer = em.merge(projectLayer);
            if (projectsIdOld != null && !projectsIdOld.equals(projectsIdNew)) {
                projectsIdOld.getProjectLayerCollection().remove(projectLayer);
                projectsIdOld = em.merge(projectsIdOld);
            }
            if (projectsIdNew != null && !projectsIdNew.equals(projectsIdOld)) {
                projectsIdNew.getProjectLayerCollection().add(projectLayer);
                projectsIdNew = em.merge(projectsIdNew);
            }
            for (Drawing drawingCollectionNewDrawing : drawingCollectionNew) {
                if (!drawingCollectionOld.contains(drawingCollectionNewDrawing)) {
                    ProjectLayer oldProjectLayersIdOfDrawingCollectionNewDrawing = drawingCollectionNewDrawing.getProjectLayersId();
                    drawingCollectionNewDrawing.setProjectLayersId(projectLayer);
                    drawingCollectionNewDrawing = em.merge(drawingCollectionNewDrawing);
                    if (oldProjectLayersIdOfDrawingCollectionNewDrawing != null && !oldProjectLayersIdOfDrawingCollectionNewDrawing.equals(projectLayer)) {
                        oldProjectLayersIdOfDrawingCollectionNewDrawing.getDrawingCollection().remove(drawingCollectionNewDrawing);
                        oldProjectLayersIdOfDrawingCollectionNewDrawing = em.merge(oldProjectLayersIdOfDrawingCollectionNewDrawing);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = projectLayer.getId();
                if (findProjectLayer(id) == null) {
                    throw new NonexistentEntityException("The projectLayer with id " + id + " no longer exists.");
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
            ProjectLayer projectLayer;
            try {
                projectLayer = em.getReference(ProjectLayer.class, id);
                projectLayer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The projectLayer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Drawing> drawingCollectionOrphanCheck = projectLayer.getDrawingCollection();
            for (Drawing drawingCollectionOrphanCheckDrawing : drawingCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProjectLayer (" + projectLayer + ") cannot be destroyed since the Drawing " + drawingCollectionOrphanCheckDrawing + " in its drawingCollection field has a non-nullable projectLayersId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Project projectsId = projectLayer.getProjectsId();
            if (projectsId != null) {
                projectsId.getProjectLayerCollection().remove(projectLayer);
                projectsId = em.merge(projectsId);
            }
            em.remove(projectLayer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProjectLayer> findProjectLayerEntities() {
        return findProjectLayerEntities(true, -1, -1);
    }

    public List<ProjectLayer> findProjectLayerEntities(int maxResults, int firstResult) {
        return findProjectLayerEntities(false, maxResults, firstResult);
    }

    private List<ProjectLayer> findProjectLayerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProjectLayer.class));
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

    public ProjectLayer findProjectLayer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProjectLayer.class, id);
        } finally {
            em.close();
        }
    }

    public int getProjectLayerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProjectLayer> rt = cq.from(ProjectLayer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
