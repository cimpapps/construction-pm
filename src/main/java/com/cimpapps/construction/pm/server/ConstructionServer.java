
package com.cimpapps.construction.pm.server;

import com.cimpapps.construction.pm.service.EmployeePositionService;
import com.cimpapps.construction.pm.service.EmployeeService;
import com.cimpapps.construction.pm.service.ProjectLayersService;
import com.cimpapps.construction.pm.service.ProjectService;
import com.cimpapps.construction.pm.service.UserService;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConstructionServer {
    
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(4334);
            registry.bind("user", UserService.getInstance());
            registry.bind("employee", EmployeeService.getInstance());
            registry.bind("employeePosition", EmployeePositionService.getInstance());
            registry.bind("project", ProjectService.getInstance());
            registry.bind("layer", ProjectLayersService.getInstance());
            System.out.println("Serverul este pornit!");
        } catch (RemoteException | AlreadyBoundException ex) {
            ex.printStackTrace();
            Logger.getLogger(ConstructionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
