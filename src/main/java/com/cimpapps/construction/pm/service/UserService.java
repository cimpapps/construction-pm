package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.UserDao;
import com.cimpapps.construction.pm.dao.exceptions.NonexistentEntityException;
import com.cimpapps.construction.pm.models.User;
import construction.pm.lib.dto.UserDTO;
import construction.pm.lib.rmi.AbstractUserRemote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;

public class UserService extends UnicastRemoteObject
        implements AbstractUserRemote {

    private static UserService SINGLETON;

    static {
        try {
            SINGLETON = new UserService();
        } catch (RemoteException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private UserDao dao;

    private UserService() throws RemoteException {
        EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance().getEntityMangerFactory();
        dao = new UserDao(emf);
    }

    public static UserService getInstance() {
        return SINGLETON;
    }

    @Override
    public UserDTO findByUsername(String username) {

        User u = dao.findUserByUsername(username);
        UserDTO userDto = new UserDTO();

        if (u != null) {
            userDto.setId(u.getId());
            userDto.setUsername(u.getUsername());
            userDto.setPassword(u.getPassword());

            return userDto;
        } else {
            return null;
        }

    }

    @Override
    public void removeUser(UserDTO userDto) {
        try {
            User u = new User();
            u.setUserFromDto(userDto);

            User userDB = dao.findUserByUsername(u.getUsername());
            if (userDB != null) {
                if (userDB.getPassword().equals(u.getPassword())) {
                    dao.destroy(userDB.getId());
                } else {
                    throw new NonexistentEntityException("we don't have this exact match registerd in database");
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean register(UserDTO userDto) {
        try {
            User u = new User();
            u.setUserFromDto(userDto);

            User userDB = dao.findUserByUsername(u.getUsername());
            if (userDB != null) {
                return false;
            }

            dao.create(u);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDTO logIn(UserDTO userDto) {
        User u = dao.findUserByUsername(userDto.getUsername());
        if (u == null) {
            return null;
        } else if (u.getPassword().equals(userDto.getPassword())) {
            return userDto;
        }
        return null;
    }
}
