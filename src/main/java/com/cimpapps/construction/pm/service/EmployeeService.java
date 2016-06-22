
package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.EmployeeDao;
import com.cimpapps.construction.pm.models.Employee;
import com.cimpapps.construction.pm.models.EmployeePosition;
import com.cimpapps.construction.pm.models.User;
import construction.pm.lib.dto.EmployeeDTO;
import construction.pm.lib.dto.EmployeePositionDTO;
import construction.pm.lib.dto.UserDTO;
import construction.pm.lib.rmi.AbstractEmployeeRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;

public class EmployeeService 
        extends UnicastRemoteObject 
        implements AbstractEmployeeRemote
{
    private static EmployeeService SINGLETON ;

    static 
    {
        try {
            SINGLETON = new EmployeeService();
        } catch (RemoteException ex) {}
    }
    
    private final EmployeeDao dao;
    
    private EmployeeService() throws RemoteException
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
        Employee employee = new Employee();
        employee.setEmail(employeeDto.getEmail());
        
        EmployeePositionDTO positionDTO = employeeDto.getEmployeePositionsId();
        EmployeePosition position = new EmployeePosition(positionDTO.getId(), positionDTO.getPosition());
        employee.setEmployeePositionsId(position);
        
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        
        User user = new User();
        UserDTO userDto = employeeDto.getUsersId();
        user.setId(userDto.getId());
        user.setUserFromDto(userDto);
        
        employee.setUsersId(user);
        
        dao.create(employee);
        return true;
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
