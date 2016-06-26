package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.ProjectDao;
import com.cimpapps.construction.pm.models.Project;
import com.cimpapps.construction.pm.service.mapping.DTOtoDBEntities;
import com.cimpapps.construction.pm.service.mapping.DbEntitiesToDtoMapper;
import construction.pm.lib.dto.ProjectDTO;
import construction.pm.lib.rmi.AbstractProjectRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class ProjectService
        extends UnicastRemoteObject 
        implements AbstractProjectRemote
{

    private static ProjectService SINGLETON;
    private ProjectDao dao;

    static {
        try {
            SINGLETON = new ProjectService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProjectService() throws RemoteException {
        EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance().getEntityMangerFactory();
        dao = new ProjectDao(emf);
    }

    public static ProjectService getInstance() {
        return SINGLETON;
    }

    @Override
    public ProjectDTO addProject(ProjectDTO projectDto) {
        try {
            Project project = DTOtoDBEntities.mapDtoToProject(projectDto);
            dao.create(project);
            return projectDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProjectDTO> getAllProjects(){
       List<ProjectDTO> projectsDto = null;
       
        try {
            List<Project> projects = dao.findProjectEntities();
            projectsDto = DbEntitiesToDtoMapper.mapProjectCollecitonToDtoCollection(projects);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
       return projectsDto;
    }
    
    
}
