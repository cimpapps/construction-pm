package com.cimpapps.construction.pm.service;

import construction.pm.lib.dto.ProjectDTO;
import construction.pm.lib.rmi.AbstractProjectRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ProjectService
        extends UnicastRemoteObject 
        implements AbstractProjectRemote
{

    private static ProjectService SINGLETON;

    static {
        try {
            SINGLETON = new ProjectService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProjectService() throws RemoteException {

    }

    public static ProjectService getInstance() {
        return SINGLETON;
    }

    @Override
    public ProjectDTO addProject(ProjectDTO projectDto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProjectDTO> getAllProjects(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
