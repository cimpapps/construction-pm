/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.service.test;

import com.cimpapps.construction.pm.service.UserService;
import construction.pm.lib.dto.UserDTO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author catalin.cimpoeru
 */
public class UserRegisterTest {
    UserService service = UserService.getInstance();
    
    public UserRegisterTest() {
    }

    @Test
    public void testRegisterUser(){
        UserDTO u = new UserDTO();
        u.setUsername("Catalin");
        u.setPassword("Cimpoeru");
        boolean register = service.register(u);
        assertTrue(register);
      
        register = service.register(u);
        assertFalse(register);
        
        
    }
    
    @Test
    public void logInTest(){
        UserDTO u = new UserDTO();
        u.setUsername("Catalin");
        u.setPassword("Cimpoeru");
        
        u = service.logIn(u);
        assertNotNull(u);
        
        service.removeUser(u);
        u = service.logIn(u);
        assertNull(u);
        
    }
    
  
}
