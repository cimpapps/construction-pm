
package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.EmployeePositionDao;
import com.cimpapps.construction.pm.models.EmployeePosition;
import construction.pm.lib.dto.EmployeePositionDTO;
import construction.pm.lib.rmi.AbstractEmployeePositionRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.persistence.EntityManagerFactory;

public class EmployeePositionService
        extends UnicastRemoteObject
        implements AbstractEmployeePositionRemote
{ 
    
    private static EmployeePositionService SINGLETON;
    
    static{
        try {
            SINGLETON =  new EmployeePositionService();
        } catch (RemoteException ex) {}
    }

    public static EmployeePositionService getInstance() {
        return SINGLETON;
    }
    
    private EmployeePositionDao dao;
    private EmployeePositionService() throws RemoteException{
        EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance().getEntityMangerFactory();
        dao = new EmployeePositionDao(emf);
    }

    @Override
    public boolean addPosition(EmployeePositionDTO employeePositionDTO) throws RemoteException {
        try {
            EmployeePosition position = new EmployeePosition();
            position.setId(employeePositionDTO.getId());
            position.setPosition(employeePositionDTO.getPosition());
            dao.create(position);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removePosition(EmployeePositionDTO employeePositionDTO) throws RemoteException {
        try {
            dao.destroy(employeePositionDTO.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}//end of class
