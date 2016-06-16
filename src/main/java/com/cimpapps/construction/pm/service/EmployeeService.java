
package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.EmployeeDao;
import construction.pm.lib.dto.EmployeeDTO;
import construction.pm.lib.rmi.AbstractEmployeeRemote;
import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;

public class EmployeeService implements AbstractEmployeeRemote{
    private static final EmployeeService SINGLETON = new EmployeeService();
    private final EmployeeDao dao;
    
    private EmployeeService()
    {
        EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance().getEntityMangerFactory();
        dao = new EmployeeDao(emf);
    }

    public static EmployeeService getInstance()
    {
        return SINGLETON;
    }

    @Override
    public boolean addEmployee(EmployeeDTO employeeDto) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeEmployee(EmployeeDTO employeeDTO) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean editEmployee(int id, EmployeeDTO employeeDTO) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
