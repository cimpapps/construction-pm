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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
