package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.ProjectLayerDao;
import com.cimpapps.construction.pm.models.Drawing;
import com.cimpapps.construction.pm.models.Employee;
import com.cimpapps.construction.pm.models.EmployeePosition;
import com.cimpapps.construction.pm.models.Project;
import com.cimpapps.construction.pm.models.ProjectCategory;
import com.cimpapps.construction.pm.models.ProjectLayer;
import com.cimpapps.construction.pm.models.User;
import com.cimpapps.construction.pm.service.mapping.DbEntitiesToDtoMapper;
import construction.pm.lib.dto.DrawingDTO;
import construction.pm.lib.dto.EmployeeDTO;
import construction.pm.lib.dto.EmployeePositionDTO;
import construction.pm.lib.dto.ProjectCategoryDTO;
import construction.pm.lib.dto.ProjectDTO;
import construction.pm.lib.dto.ProjectLayerDTO;
import construction.pm.lib.dto.UserDTO;
import construction.pm.lib.rmi.AbstractLayersRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;

public class ProjectLayersService
        extends UnicastRemoteObject
        implements AbstractLayersRemote {

    private static ProjectLayersService SINGLETON;
    private ProjectLayerDao dao;

    static {
        try {
            SINGLETON = new ProjectLayersService();
        } catch (RemoteException ex) {
            Logger.getLogger(ProjectLayersService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ProjectLayersService() throws RemoteException {
        EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance().getEntityMangerFactory();
        dao = new ProjectLayerDao(emf);
    }

    public static ProjectLayersService getInstance() {
        return SINGLETON;
    }

    @Override
    public boolean addLayer(ProjectLayerDTO layer) throws RemoteException {
        try {
            ProjectLayer l = new ProjectLayer();
            l.setName(layer.getName());
            Project project = new Project();
            ProjectDTO prDto = layer.getProjectsId();
            project.setId(prDto.getId());
            l.setProjectsId(project);
            dao.create(l);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ProjectLayerDTO> getAllLayers() throws RemoteException {
        List<ProjectLayerDTO> layersDto = null;
        try {
            List<ProjectLayer> layers = dao.findProjectLayerEntities();
            layersDto = (List<ProjectLayerDTO>) DbEntitiesToDtoMapper.mapLayerCollectionToDtoCollection(layers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layersDto;
    }

}
