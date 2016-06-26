package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.ProjectLayerDao;
import com.cimpapps.construction.pm.models.Project;
import com.cimpapps.construction.pm.models.ProjectLayer;
import com.cimpapps.construction.pm.service.mapping.DTOtoDBEntities;
import com.cimpapps.construction.pm.service.mapping.DbEntitiesToDtoMapper;
import construction.pm.lib.dto.ProjectDTO;
import construction.pm.lib.dto.ProjectLayerDTO;
import construction.pm.lib.rmi.AbstractLayersRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
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
            ProjectLayer l = DTOtoDBEntities.mapLayerToDto(layer);            
            dao.create(l);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ProjectLayerDTO> getAllLayers() {
        List<ProjectLayerDTO> layersDto = null;
        try {
            List<ProjectLayer> layers = dao.findProjectLayerEntities();
            layersDto = (List<ProjectLayerDTO>) DbEntitiesToDtoMapper.mapLayerCollectionToDtoCollection(layers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layersDto;
    }

    @Override
    public List<ProjectLayerDTO> getProjectLayers(ProjectDTO projectDto){
        
        if(projectDto == null)
            return null;
        
        List<ProjectLayerDTO> layers = getAllLayers();
        List<ProjectLayerDTO> projectLayers = new ArrayList<>();
        
        for (ProjectLayerDTO layer : layers) {
            
            if( layer.getProjectsId().getId().toString().equals(projectDto.getId().toString()))
                projectLayers.add(layer);
            else
                System.out.println(layer.getProjectsId().getId() + "    " + projectDto.getId() );
        }
        
        System.out.println(projectLayers);
        
        return projectLayers;
    }
    
}
