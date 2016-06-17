
package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.EmployeePositionDao;
import com.cimpapps.construction.pm.models.EmployeePosition;
import construction.pm.lib.dto.EmployeePositionDTO;
import construction.pm.lib.rmi.AbstractEmployeePositionRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;

public class EmployeePositionService
        extends UnicastRemoteObject
        implements AbstractEmployeePositionRemote
{ 
    
    private static EmployeePositionService SINGLETON;
    private EmployeePositionDao dao;
    static{
        try {
            SINGLETON =  new EmployeePositionService();
        } catch (RemoteException ex) {}
    }

    public static EmployeePositionService getInstance() {
        return SINGLETON;
    }
    
    
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

    @Override
    public List<EmployeePositionDTO> getAllEmployeePositions() throws RemoteException {
        List<EmployeePositionDTO> employeesPosDTO = null;
        
        List<EmployeePosition> employeesPos = dao.findEmployeePositionEntities();
        employeesPosDTO = employeesPos.stream()
                .map(e -> {
                    EmployeePositionDTO dto = new EmployeePositionDTO();
                    dto.setId(e.getId());
                    dto.setPosition(e.getPosition());
                    return dto;})
                .collect(Collectors.toList());
                    
        return employeesPosDTO;
    }
    
}//end of class
