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
    
    public UserRegisterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testRegisterUser(){
        UserDTO u = new UserDTO();
        u.setUsername("catalin");
        u.setPassword("cimpoeru");
        
        UserService service = UserService.getInstance();
        
        boolean register = service.register(u);
        assertTrue(register);
        
        register = service.register(u);
        assertFalse(register);
        
        service.removeUser(u);
    }
}
