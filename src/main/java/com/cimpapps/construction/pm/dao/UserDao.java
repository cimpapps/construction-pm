
package com.cimpapps.construction.pm.dao;

import com.cimpapps.construction.pm.dao.exceptions.IllegalOrphanException;
import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cimpapps.construction.pm.models.Employee;
import com.cimpapps.construction.pm.models.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UserDao implements Serializable {

    public UserDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getEmployeeCollection() == null) {
            user.setEmployeeCollection(new ArrayList<Employee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Employee> attachedEmployeeCollection = new ArrayList<Employee>();
            for (Employee employeeCollectionEmployeeToAttach : user.getEmployeeCollection()) {
                employeeCollectionEmployeeToAttach = em.getReference(employeeCollectionEmployeeToAttach.getClass(), employeeCollectionEmployeeToAttach.getId());
                attachedEmployeeCollection.add(employeeCollectionEmployeeToAttach);
            }
            user.setEmployeeCollection(attachedEmployeeCollection);
            em.persist(user);
            for (Employee employeeCollectionEmployee : user.getEmployeeCollection()) {
                User oldUsersIdOfEmployeeCollectionEmployee = employeeCollectionEmployee.getUsersId();
                employeeCollectionEmployee.setUsersId(user);
                employeeCollectionEmployee = em.merge(employeeCollectionEmployee);
                if (oldUsersIdOfEmployeeCollectionEmployee != null) {
                    oldUsersIdOfEmployeeCollectionEmployee.getEmployeeCollection().remove(employeeCollectionEmployee);
                    oldUsersIdOfEmployeeCollectionEmployee = em.merge(oldUsersIdOfEmployeeCollectionEmployee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Collection<Employee> employeeCollectionOld = persistentUser.getEmployeeCollection();
            Collection<Employee> employeeCollectionNew = user.getEmployeeCollection();
            List<String> illegalOrphanMessages = null;
            for (Employee employeeCollectionOldEmployee : employeeCollectionOld) {
                if (!employeeCollectionNew.contains(employeeCollectionOldEmployee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Employee " + employeeCollectionOldEmployee + " since its usersId field is not nullable.");
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
            user.setEmployeeCollection(employeeCollectionNew);
            user = em.merge(user);
            for (Employee employeeCollectionNewEmployee : employeeCollectionNew) {
                if (!employeeCollectionOld.contains(employeeCollectionNewEmployee)) {
                    User oldUsersIdOfEmployeeCollectionNewEmployee = employeeCollectionNewEmployee.getUsersId();
                    employeeCollectionNewEmployee.setUsersId(user);
                    employeeCollectionNewEmployee = em.merge(employeeCollectionNewEmployee);
                    if (oldUsersIdOfEmployeeCollectionNewEmployee != null && !oldUsersIdOfEmployeeCollectionNewEmployee.equals(user)) {
                        oldUsersIdOfEmployeeCollectionNewEmployee.getEmployeeCollection().remove(employeeCollectionNewEmployee);
                        oldUsersIdOfEmployeeCollectionNewEmployee = em.merge(oldUsersIdOfEmployeeCollectionNewEmployee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Employee> employeeCollectionOrphanCheck = user.getEmployeeCollection();
            for (Employee employeeCollectionOrphanCheckEmployee : employeeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Employee " + employeeCollectionOrphanCheckEmployee + " in its employeeCollection field has a non-nullable usersId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
