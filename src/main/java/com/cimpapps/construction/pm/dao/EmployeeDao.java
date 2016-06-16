
package com.cimpapps.construction.pm.dao;

import com.cimpapps.construction.pm.dao.exceptions.IllegalOrphanException;
import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cimpapps.construction.pm.models.EmployeePosition;
import com.cimpapps.construction.pm.models.User;
import com.cimpapps.construction.pm.models.Project;
import java.util.ArrayList;
import java.util.Collection;
import com.cimpapps.construction.pm.models.Drawing;
import com.cimpapps.construction.pm.models.Employee;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EmployeeDao implements Serializable {

    public EmployeeDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) {
        if (employee.getProjectCollection() == null) {
            employee.setProjectCollection(new ArrayList<Project>());
        }
        if (employee.getDrawingCollection() == null) {
            employee.setDrawingCollection(new ArrayList<Drawing>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmployeePosition employeePositionsId = employee.getEmployeePositionsId();
            if (employeePositionsId != null) {
                employeePositionsId = em.getReference(employeePositionsId.getClass(), employeePositionsId.getId());
                employee.setEmployeePositionsId(employeePositionsId);
            }
            User usersId = employee.getUsersId();
            if (usersId != null) {
                usersId = em.getReference(usersId.getClass(), usersId.getId());
                employee.setUsersId(usersId);
            }
            Collection<Project> attachedProjectCollection = new ArrayList<Project>();
            for (Project projectCollectionProjectToAttach : employee.getProjectCollection()) {
                projectCollectionProjectToAttach = em.getReference(projectCollectionProjectToAttach.getClass(), projectCollectionProjectToAttach.getId());
                attachedProjectCollection.add(projectCollectionProjectToAttach);
            }
            employee.setProjectCollection(attachedProjectCollection);
            Collection<Drawing> attachedDrawingCollection = new ArrayList<Drawing>();
            for (Drawing drawingCollectionDrawingToAttach : employee.getDrawingCollection()) {
                drawingCollectionDrawingToAttach = em.getReference(drawingCollectionDrawingToAttach.getClass(), drawingCollectionDrawingToAttach.getId());
                attachedDrawingCollection.add(drawingCollectionDrawingToAttach);
            }
            employee.setDrawingCollection(attachedDrawingCollection);
            em.persist(employee);
            if (employeePositionsId != null) {
                employeePositionsId.getEmployeeCollection().add(employee);
                employeePositionsId = em.merge(employeePositionsId);
            }
            if (usersId != null) {
                usersId.getEmployeeCollection().add(employee);
                usersId = em.merge(usersId);
            }
            for (Project projectCollectionProject : employee.getProjectCollection()) {
                projectCollectionProject.getEmployeeCollection().add(employee);
                projectCollectionProject = em.merge(projectCollectionProject);
            }
            for (Drawing drawingCollectionDrawing : employee.getDrawingCollection()) {
                Employee oldEmployeesIdOfDrawingCollectionDrawing = drawingCollectionDrawing.getEmployeesId();
                drawingCollectionDrawing.setEmployeesId(employee);
                drawingCollectionDrawing = em.merge(drawingCollectionDrawing);
                if (oldEmployeesIdOfDrawingCollectionDrawing != null) {
                    oldEmployeesIdOfDrawingCollectionDrawing.getDrawingCollection().remove(drawingCollectionDrawing);
                    oldEmployeesIdOfDrawingCollectionDrawing = em.merge(oldEmployeesIdOfDrawingCollectionDrawing);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Employee employee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee persistentEmployee = em.find(Employee.class, employee.getId());
            EmployeePosition employeePositionsIdOld = persistentEmployee.getEmployeePositionsId();
            EmployeePosition employeePositionsIdNew = employee.getEmployeePositionsId();
            User usersIdOld = persistentEmployee.getUsersId();
            User usersIdNew = employee.getUsersId();
            Collection<Project> projectCollectionOld = persistentEmployee.getProjectCollection();
            Collection<Project> projectCollectionNew = employee.getProjectCollection();
            Collection<Drawing> drawingCollectionOld = persistentEmployee.getDrawingCollection();
            Collection<Drawing> drawingCollectionNew = employee.getDrawingCollection();
            List<String> illegalOrphanMessages = null;
            for (Drawing drawingCollectionOldDrawing : drawingCollectionOld) {
                if (!drawingCollectionNew.contains(drawingCollectionOldDrawing)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Drawing " + drawingCollectionOldDrawing + " since its employeesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (employeePositionsIdNew != null) {
                employeePositionsIdNew = em.getReference(employeePositionsIdNew.getClass(), employeePositionsIdNew.getId());
                employee.setEmployeePositionsId(employeePositionsIdNew);
            }
            if (usersIdNew != null) {
                usersIdNew = em.getReference(usersIdNew.getClass(), usersIdNew.getId());
                employee.setUsersId(usersIdNew);
            }
            Collection<Project> attachedProjectCollectionNew = new ArrayList<Project>();
            for (Project projectCollectionNewProjectToAttach : projectCollectionNew) {
                projectCollectionNewProjectToAttach = em.getReference(projectCollectionNewProjectToAttach.getClass(), projectCollectionNewProjectToAttach.getId());
                attachedProjectCollectionNew.add(projectCollectionNewProjectToAttach);
            }
            projectCollectionNew = attachedProjectCollectionNew;
            employee.setProjectCollection(projectCollectionNew);
            Collection<Drawing> attachedDrawingCollectionNew = new ArrayList<Drawing>();
            for (Drawing drawingCollectionNewDrawingToAttach : drawingCollectionNew) {
                drawingCollectionNewDrawingToAttach = em.getReference(drawingCollectionNewDrawingToAttach.getClass(), drawingCollectionNewDrawingToAttach.getId());
                attachedDrawingCollectionNew.add(drawingCollectionNewDrawingToAttach);
            }
            drawingCollectionNew = attachedDrawingCollectionNew;
            employee.setDrawingCollection(drawingCollectionNew);
            employee = em.merge(employee);
            if (employeePositionsIdOld != null && !employeePositionsIdOld.equals(employeePositionsIdNew)) {
                employeePositionsIdOld.getEmployeeCollection().remove(employee);
                employeePositionsIdOld = em.merge(employeePositionsIdOld);
            }
            if (employeePositionsIdNew != null && !employeePositionsIdNew.equals(employeePositionsIdOld)) {
                employeePositionsIdNew.getEmployeeCollection().add(employee);
                employeePositionsIdNew = em.merge(employeePositionsIdNew);
            }
            if (usersIdOld != null && !usersIdOld.equals(usersIdNew)) {
                usersIdOld.getEmployeeCollection().remove(employee);
                usersIdOld = em.merge(usersIdOld);
            }
            if (usersIdNew != null && !usersIdNew.equals(usersIdOld)) {
                usersIdNew.getEmployeeCollection().add(employee);
                usersIdNew = em.merge(usersIdNew);
            }
            for (Project projectCollectionOldProject : projectCollectionOld) {
                if (!projectCollectionNew.contains(projectCollectionOldProject)) {
                    projectCollectionOldProject.getEmployeeCollection().remove(employee);
                    projectCollectionOldProject = em.merge(projectCollectionOldProject);
                }
            }
            for (Project projectCollectionNewProject : projectCollectionNew) {
                if (!projectCollectionOld.contains(projectCollectionNewProject)) {
                    projectCollectionNewProject.getEmployeeCollection().add(employee);
                    projectCollectionNewProject = em.merge(projectCollectionNewProject);
                }
            }
            for (Drawing drawingCollectionNewDrawing : drawingCollectionNew) {
                if (!drawingCollectionOld.contains(drawingCollectionNewDrawing)) {
                    Employee oldEmployeesIdOfDrawingCollectionNewDrawing = drawingCollectionNewDrawing.getEmployeesId();
                    drawingCollectionNewDrawing.setEmployeesId(employee);
                    drawingCollectionNewDrawing = em.merge(drawingCollectionNewDrawing);
                    if (oldEmployeesIdOfDrawingCollectionNewDrawing != null && !oldEmployeesIdOfDrawingCollectionNewDrawing.equals(employee)) {
                        oldEmployeesIdOfDrawingCollectionNewDrawing.getDrawingCollection().remove(drawingCollectionNewDrawing);
                        oldEmployeesIdOfDrawingCollectionNewDrawing = em.merge(oldEmployeesIdOfDrawingCollectionNewDrawing);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = employee.getId();
                if (findEmployee(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
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
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Drawing> drawingCollectionOrphanCheck = employee.getDrawingCollection();
            for (Drawing drawingCollectionOrphanCheckDrawing : drawingCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Drawing " + drawingCollectionOrphanCheckDrawing + " in its drawingCollection field has a non-nullable employeesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EmployeePosition employeePositionsId = employee.getEmployeePositionsId();
            if (employeePositionsId != null) {
                employeePositionsId.getEmployeeCollection().remove(employee);
                employeePositionsId = em.merge(employeePositionsId);
            }
            User usersId = employee.getUsersId();
            if (usersId != null) {
                usersId.getEmployeeCollection().remove(employee);
                usersId = em.merge(usersId);
            }
            Collection<Project> projectCollection = employee.getProjectCollection();
            for (Project projectCollectionProject : projectCollection) {
                projectCollectionProject.getEmployeeCollection().remove(employee);
                projectCollectionProject = em.merge(projectCollectionProject);
            }
            em.remove(employee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
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

    public Employee findEmployee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
