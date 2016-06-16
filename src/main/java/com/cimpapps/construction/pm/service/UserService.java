package com.cimpapps.construction.pm.service;

import com.cimpapps.construction.pm.dao.UserDao;
import com.cimpapps.construction.pm.models.User;
import construction.pm.lib.dto.UserDTO;
import construction.pm.lib.rmi.AbstractUserRemote;
import javax.persistence.EntityManagerFactory;

public class UserService implements AbstractUserRemote {

    private static final UserService SINGLETON = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
        return SINGLETON;
    }

    @Override
    public boolean register(UserDTO userDto) {
        try {
            EntityManagerFactory emf = EntityManagerFactorySingleton.getInstance().getEntityMangerFactory();
            UserDao dao = new UserDao(emf);
            User u = new User();
            u.setUsername(userDto.getUsername());
            u.setPassword(userDto.getPassword());
            
            User userDB = dao.findUserByUsername(u.getUsername());
            if (userDB != null)
                return false;
            
            dao.create(u);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserDTO logIn(UserDTO user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
