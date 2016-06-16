
package com.cimpapps.construction.pm.dao;

import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import com.cimpapps.construction.pm.dao.exceptions.PreexistingEntityException;
import com.cimpapps.construction.pm.models.Drawing;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cimpapps.construction.pm.models.Employee;
import com.cimpapps.construction.pm.models.ProjectLayer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class DrawingDao implements Serializable {

    public DrawingDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Drawing drawing) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employeesId = drawing.getEmployeesId();
            if (employeesId != null) {
                employeesId = em.getReference(employeesId.getClass(), employeesId.getId());
                drawing.setEmployeesId(employeesId);
            }
            ProjectLayer projectLayersId = drawing.getProjectLayersId();
            if (projectLayersId != null) {
                projectLayersId = em.getReference(projectLayersId.getClass(), projectLayersId.getId());
                drawing.setProjectLayersId(projectLayersId);
            }
            em.persist(drawing);
            if (employeesId != null) {
                employeesId.getDrawingCollection().add(drawing);
                employeesId = em.merge(employeesId);
            }
            if (projectLayersId != null) {
                projectLayersId.getDrawingCollection().add(drawing);
                projectLayersId = em.merge(projectLayersId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDrawing(drawing.getId()) != null) {
                throw new PreexistingEntityException("Drawing " + drawing + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Drawing drawing) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Drawing persistentDrawing = em.find(Drawing.class, drawing.getId());
            Employee employeesIdOld = persistentDrawing.getEmployeesId();
            Employee employeesIdNew = drawing.getEmployeesId();
            ProjectLayer projectLayersIdOld = persistentDrawing.getProjectLayersId();
            ProjectLayer projectLayersIdNew = drawing.getProjectLayersId();
            if (employeesIdNew != null) {
                employeesIdNew = em.getReference(employeesIdNew.getClass(), employeesIdNew.getId());
                drawing.setEmployeesId(employeesIdNew);
            }
            if (projectLayersIdNew != null) {
                projectLayersIdNew = em.getReference(projectLayersIdNew.getClass(), projectLayersIdNew.getId());
                drawing.setProjectLayersId(projectLayersIdNew);
            }
            drawing = em.merge(drawing);
            if (employeesIdOld != null && !employeesIdOld.equals(employeesIdNew)) {
                employeesIdOld.getDrawingCollection().remove(drawing);
                employeesIdOld = em.merge(employeesIdOld);
            }
            if (employeesIdNew != null && !employeesIdNew.equals(employeesIdOld)) {
                employeesIdNew.getDrawingCollection().add(drawing);
                employeesIdNew = em.merge(employeesIdNew);
            }
            if (projectLayersIdOld != null && !projectLayersIdOld.equals(projectLayersIdNew)) {
                projectLayersIdOld.getDrawingCollection().remove(drawing);
                projectLayersIdOld = em.merge(projectLayersIdOld);
            }
            if (projectLayersIdNew != null && !projectLayersIdNew.equals(projectLayersIdOld)) {
                projectLayersIdNew.getDrawingCollection().add(drawing);
                projectLayersIdNew = em.merge(projectLayersIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = drawing.getId();
                if (findDrawing(id) == null) {
                    throw new NonexistentEntityException("The drawing with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Drawing drawing;
            try {
                drawing = em.getReference(Drawing.class, id);
                drawing.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The drawing with id " + id + " no longer exists.", enfe);
            }
            Employee employeesId = drawing.getEmployeesId();
            if (employeesId != null) {
                employeesId.getDrawingCollection().remove(drawing);
                employeesId = em.merge(employeesId);
            }
            ProjectLayer projectLayersId = drawing.getProjectLayersId();
            if (projectLayersId != null) {
                projectLayersId.getDrawingCollection().remove(drawing);
                projectLayersId = em.merge(projectLayersId);
            }
            em.remove(drawing);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Drawing> findDrawingEntities() {
        return findDrawingEntities(true, -1, -1);
    }

    public List<Drawing> findDrawingEntities(int maxResults, int firstResult) {
        return findDrawingEntities(false, maxResults, firstResult);
    }

    private List<Drawing> findDrawingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Drawing.class));
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

    public Drawing findDrawing(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Drawing.class, id);
        } finally {
            em.close();
        }
    }

    public int getDrawingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Drawing> rt = cq.from(Drawing.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
