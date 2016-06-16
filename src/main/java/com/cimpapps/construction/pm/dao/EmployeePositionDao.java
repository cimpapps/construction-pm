
package com.cimpapps.construction.pm.dao;

import com.cimpapps.construction.pm.dao.exceptions.IllegalOrphanException;
import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cimpapps.construction.pm.models.Employee;
import com.cimpapps.construction.pm.models.EmployeePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EmployeePositionDao implements Serializable {

    public EmployeePositionDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmployeePosition employeePosition) {
        if (employeePosition.getEmployeeCollection() == null) {
            employeePosition.setEmployeeCollection(new ArrayList<Employee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Employee> attachedEmployeeCollection = new ArrayList<Employee>();
            for (Employee employeeCollectionEmployeeToAttach : employeePosition.getEmployeeCollection()) {
                employeeCollectionEmployeeToAttach = em.getReference(employeeCollectionEmployeeToAttach.getClass(), employeeCollectionEmployeeToAttach.getId());
                attachedEmployeeCollection.add(employeeCollectionEmployeeToAttach);
            }
            employeePosition.setEmployeeCollection(attachedEmployeeCollection);
            em.persist(employeePosition);
            for (Employee employeeCollectionEmployee : employeePosition.getEmployeeCollection()) {
                EmployeePosition oldEmployeePositionsIdOfEmployeeCollectionEmployee = employeeCollectionEmployee.getEmployeePositionsId();
                employeeCollectionEmployee.setEmployeePositionsId(employeePosition);
                employeeCollectionEmployee = em.merge(employeeCollectionEmployee);
                if (oldEmployeePositionsIdOfEmployeeCollectionEmployee != null) {
                    oldEmployeePositionsIdOfEmployeeCollectionEmployee.getEmployeeCollection().remove(employeeCollectionEmployee);
                    oldEmployeePositionsIdOfEmployeeCollectionEmployee = em.merge(oldEmployeePositionsIdOfEmployeeCollectionEmployee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmployeePosition employeePosition) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmployeePosition persistentEmployeePosition = em.find(EmployeePosition.class, employeePosition.getId());
            Collection<Employee> employeeCollectionOld = persistentEmployeePosition.getEmployeeCollection();
            Collection<Employee> employeeCollectionNew = employeePosition.getEmployeeCollection();
            List<String> illegalOrphanMessages = null;
            for (Employee employeeCollectionOldEmployee : employeeCollectionOld) {
                if (!employeeCollectionNew.contains(employeeCollectionOldEmployee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Employee " + employeeCollectionOldEmployee + " since its employeePositionsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Employee> attachedEmployeeCollectionNew = new ArrayList<Employee>();
            for (Employee employeeCollectionNewEmployeeToAttach : employeeCollectionNew) {
                employeeCollectionNewEmployeeToAttach = em.getReference(employeeCollectionNewEmployeeToAttach.getClass(), employeeCollectionNewEmployeeToAttach.getId());
                attachedEmployeeCollectionNew.add(employeeCollectionNewEmployeeToAttach);
            }
            employeeCollectionNew = attachedEmployeeCollectionNew;
            employeePosition.setEmployeeCollection(employeeCollectionNew);
            employeePosition = em.merge(employeePosition);
            for (Employee employeeCollectionNewEmployee : employeeCollectionNew) {
                if (!employeeCollectionOld.contains(employeeCollectionNewEmployee)) {
                    EmployeePosition oldEmployeePositionsIdOfEmployeeCollectionNewEmployee = employeeCollectionNewEmployee.getEmployeePositionsId();
                    employeeCollectionNewEmployee.setEmployeePositionsId(employeePosition);
                    employeeCollectionNewEmployee = em.merge(employeeCollectionNewEmployee);
                    if (oldEmployeePositionsIdOfEmployeeCollectionNewEmployee != null && !oldEmployeePositionsIdOfEmployeeCollectionNewEmployee.equals(employeePosition)) {
                        oldEmployeePositionsIdOfEmployeeCollectionNewEmployee.getEmployeeCollection().remove(employeeCollectionNewEmployee);
                        oldEmployeePositionsIdOfEmployeeCollectionNewEmployee = em.merge(oldEmployeePositionsIdOfEmployeeCollectionNewEmployee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = employeePosition.getId();
                if (findEmployeePosition(id) == null) {
                    throw new NonexistentEntityException("The employeePosition with id " + id + " no longer exists.");
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
            EmployeePosition employeePosition;
            try {
                employeePosition = em.getReference(EmployeePosition.class, id);
                employeePosition.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employeePosition with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Employee> employeeCollectionOrphanCheck = employeePosition.getEmployeeCollection();
            for (Employee employeeCollectionOrphanCheckEmployee : employeeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EmployeePosition (" + employeePosition + ") cannot be destroyed since the Employee " + employeeCollectionOrphanCheckEmployee + " in its employeeCollection field has a non-nullable employeePositionsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(employeePosition);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EmployeePosition> findEmployeePositionEntities() {
        return findEmployeePositionEntities(true, -1, -1);
    }

    public List<EmployeePosition> findEmployeePositionEntities(int maxResults, int firstResult) {
        return findEmployeePositionEntities(false, maxResults, firstResult);
    }

    private List<EmployeePosition> findEmployeePositionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmployeePosition.class));
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

    public EmployeePosition findEmployeePosition(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmployeePosition.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeePositionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmployeePosition> rt = cq.from(EmployeePosition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
